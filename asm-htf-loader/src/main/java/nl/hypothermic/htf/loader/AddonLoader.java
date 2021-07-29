package nl.hypothermic.htf.loader;

import nl.hypothermic.htf.api.ClassTransformer;
import nl.hypothermic.htf.api.Gateway;
import nl.hypothermic.htf.api.MethodTransformer;
import nl.hypothermic.htf.api.Predicates;

import java.io.*;
import java.lang.instrument.Instrumentation;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public final class AddonLoader {

	private static class InstanceHolder {

		private static final AddonLoader INSTANCE = new AddonLoader();

	}

	public static AddonLoader getInstance() {
		return AddonLoader.InstanceHolder.INSTANCE;
	}

	private static final String RELATIVE_INDEX_PATH = "META-INF/asm-htf-addons/";

	private final ReentrantLock registryLock = new ReentrantLock();

	private AddonLoader() {

	}

	public void load(String args, Instrumentation instrumentation) {
		Arrays.stream(args.split(":"))
				.map(File::new)
				.filter(File::exists)
				.filter(File::canRead)
				.forEach(file -> {
					L.i("Loading addon %s", L.getMostDescriptivePath(file));

					loadAddon(file, instrumentation);
				});
	}

	private void loadAddon(File file, Instrumentation instrumentation) {
		try {
			URLClassLoader classLoader = new URLClassLoader(new URL[]{file.toURI().toURL()});

			ZipFile zipFile = new ZipFile(file);

			Stream<? extends ZipEntry> entries = zipFile.stream()
					.filter(entry -> entry.getName().startsWith(RELATIVE_INDEX_PATH))
					.filter(Predicates.not(ZipEntry::isDirectory));

			registryLock.lock();

			entries.forEach(entry -> {
				try {
					String text = new BufferedReader(new InputStreamReader(zipFile.getInputStream(entry), StandardCharsets.UTF_8))
							.lines()
							.collect(Collectors.joining(":"));

					Stream<String> paths = Arrays.stream(text.split(":"))
							.filter(transformerPath -> transformerPath.length() > 0);

					if (entry.getName().endsWith(MethodTransformer.class.getName())) {
						paths.map(transformerPath -> classOrNull(transformerPath, classLoader))
								.filter(Objects::nonNull)
								.filter(transformerClass -> transformerClass.isAnnotationPresent(MethodTransformer.class))
								.map(MethodTransformerImpl::new)
								.sorted(MethodTransformerPriorityComparator::sort)
								.forEach(transformer -> instrumentation.addTransformer(new DynamicMethodTransformer(transformer), true));
					} else if (entry.getName().endsWith(ClassTransformer.class.getName())) {
						paths.map(transformerPath -> classOrNull(transformerPath, classLoader))
								.filter(Objects::nonNull)
								.filter(transformerClass -> transformerClass.isAnnotationPresent(ClassTransformer.class))
								.map(ClassTransformerImpl::new)
								.sorted(ClassTransformerPriorityComparator::sort)
								.forEach(transformer -> instrumentation.addTransformer(new DynamicClassTransformer(transformer), true));
					} else if (entry.getName().endsWith(Gateway.class.getName())) {
						paths.map(GatewayMethod::new)
								.filter(gatewayMethod -> gatewayMethod.isValid(classLoader))
								.sorted(GatewayAnnotationPriorityComparator::sort)
								.forEachOrdered(gateway -> GatewayManager.GATEWAYS.put(gateway.getAnnotation().id(), gateway.getMethod()));

					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			});

			registryLock.unlock();
		} catch (Exception e) {
			L.e(e, "Error while loading addon %s", file.getName());
		}
	}

	private static Class<?> classOrNull(String path, ClassLoader classLoader) {
		try {
			return Class.forName(path, false, classLoader);
		} catch (Exception e) {
			return null;
		}
	}


}
