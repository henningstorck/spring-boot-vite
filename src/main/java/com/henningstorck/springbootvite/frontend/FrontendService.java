package com.henningstorck.springbootvite.frontend;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class FrontendService {
	public static final String DEV_SERVER_HOST = "localhost";
	public static final int DEV_SERVER_PORT = 5173;
	public static final String ENTRY_POINT = "src/main.ts";
	public static final List<String> DEV_SERVER_ASSETS = List.of("http://localhost:5173/@vite/client",
		"http://localhost:5173/" + ENTRY_POINT);
	public static final String VITE_MANIFEST = "static/.vite/manifest.json";

	private final Logger logger = LoggerFactory.getLogger(FrontendService.class);
	private final FrontendProperties frontendProperties;
	private final ServletContext servletContext;

	public FrontendService(FrontendProperties frontendProperties, ServletContext servletContext) {
		this.frontendProperties = frontendProperties;
		this.servletContext = servletContext;
	}

	public List<String> getStylesheets() {
		return getAssets().stream().filter(asset -> asset.endsWith(".css")).toList();
	}

	public List<String> getScripts() {
		return getAssets().stream().filter(asset -> !asset.endsWith(".css")).toList();
	}

	private List<String> getAssets() {
		if (frontendProperties.isDevelopmentMode() && isDevServerRunning()) {
			return DEV_SERVER_ASSETS;
		}

		Optional<FrontendManifest> optionalManifest = readManifest();

		if (optionalManifest.isEmpty()) {
			return Collections.emptyList();
		}

		FrontendManifest manifest = optionalManifest.get();
		FrontendManifestEntry manifestEntry = manifest.get(ENTRY_POINT);

		List<String> assets = new ArrayList<>();
		assets.add(manifestEntry.file());

		if (manifestEntry.css() != null) {
			assets.addAll(manifestEntry.css());
		}

		return assets.stream().map(asset -> servletContext.getContextPath() + "/" + asset).toList();
	}

	public Optional<FrontendManifest> readManifest() {
		ObjectMapper objectMapper = new ObjectMapper();

		try (InputStream in = getClass().getClassLoader().getResourceAsStream(VITE_MANIFEST)) {
			byte[] bytes = StreamUtils.copyToByteArray(in);
			FrontendManifest manifest = objectMapper.readValue(bytes, FrontendManifest.class);
			return Optional.of(manifest);
		} catch (IOException e) {
			logger.error("Cannot read manifest.", e);
			return Optional.empty();
		}
	}

	private boolean isDevServerRunning() {
		try (Socket socket = new Socket(DEV_SERVER_HOST, DEV_SERVER_PORT)) {
			return true;
		} catch (IOException e) {
			return false;
		}
	}
}
