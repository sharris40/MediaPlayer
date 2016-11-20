package edu.uco.map2016.mediaplayer.services;

import java.util.LinkedList;
import java.util.List;

import edu.uco.map2016.mediaplayer.services.providers.spotify.SpotifyService;

public class ProviderRegistry {
    private LinkedList<Class<? extends ProviderService>> mServices = new LinkedList<>();

    private ProviderRegistry() {
        mServices.add(SpotifyService.class);
    }

    public List<Class<? extends ProviderService>> getServices() {
        return new LinkedList<>(mServices);
    }

    private static final ProviderRegistry mRegistry = new ProviderRegistry();

    public static ProviderRegistry getInstance() {
        return mRegistry;
    }
}
