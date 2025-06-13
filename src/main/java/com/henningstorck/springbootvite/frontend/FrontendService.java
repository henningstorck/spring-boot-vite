package com.henningstorck.springbootvite.frontend;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class FrontendService {
	public static final String DEV_SERVER_HOST = "localhost";
	public static final int DEV_SERVER_PORT = 5173;
	public static final List<String> DEV_SERVER_ASSETS = List.of("http://localhost:5173/@vite/client", "http://localhost:5173/src/main.ts");

	private final Logger logger = LoggerFactory.getLogger(FrontendService.class);

	public List<String> getStylesheets() {
		return getAssets().stream().filter(asset -> asset.endsWith(".css")).toList();
	}

	public List<String> getScripts() {
		return getAssets().stream().filter(asset -> !asset.endsWith(".css")).toList();
	}

	private List<String> getAssets() {
		if (isDevServerRunning()) {
			return DEV_SERVER_ASSETS;
		}

		Optional<FrontendManifest> optionalManifest = readManifest();

		if (optionalManifest.isEmpty()) {
			return Collections.emptyList();
		}

		FrontendManifest manifest = optionalManifest.get();
		return manifest.values().stream().map(FrontendManifestEntry::file).toList();
	}

	public Optional<FrontendManifest> readManifest() {
		ObjectMapper objectMapper = new ObjectMapper();

		try (InputStream in = getClass().getClassLoader().getResourceAsStream("static/.vite/manifest.json")) {
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
