package edu.brown.cs.student.main.acsData;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

public class CachingCensusData implements ACSDatasource {
  private final ACSDatasource original;
  private final LoadingCache<String, String> cache;

  public CachingCensusData(ACSDatasource og, int size, int minutes) {
    this.original = og;
    this.cache =
        CacheBuilder.newBuilder()
            .maximumSize(size)
            .expireAfterWrite(minutes, TimeUnit.MINUTES)
            .recordStats()
            .build(
                new CacheLoader<>() {
                  @Override
                  public String load(String key)
                      throws URISyntaxException, IOException, InterruptedException {
                    System.out.println("called load for: " + key);
                    // If this isn't yet present in the cache, load it:
                    return original.getBroadband(key);
                  }
                });
  }

  @Override
  public String getBroadband(String key) {
    System.out.println("starting cache work");
    String result = cache.getUnchecked(key);
    System.out.println("done cache work");
    return result;
  }
}
