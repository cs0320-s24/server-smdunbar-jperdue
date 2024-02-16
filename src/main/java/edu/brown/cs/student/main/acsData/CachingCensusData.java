package edu.brown.cs.student.main.acsData;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.UncheckedExecutionException;
import edu.brown.cs.student.main.api_exceptions.DatasourceException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

public class CachingCensusData implements ACSDatasource {
  private final ACSDatasource original;
  private final LoadingCache<String, String> cache;

  /**
   * @param og - original datasource that will be called if the key is not in the cache
   * @param size - maximum size of the cache
   * @param minutes - total time that items stay in the cache
   */
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
                      throws URISyntaxException, IOException, InterruptedException,
                          DatasourceException {
                    System.out.println("called load for: " + key);
                    // If this isn't yet present in the cache, load it:
                    return original.getBroadband(key);
                  }
                });
  }

  /**
   * gets the broadband data for the given key from cache or original datasource(county and state)
   *
   * @param key is the county a comma then the state provided
   * @return String of the broadband data from either the cache or the original datasource
   * @throws DatasourceException when there is an unchecked exception in the caching
   */
  @Override
  public String getBroadband(String key) throws DatasourceException {
    String result;
    try {
      result = cache.getUnchecked(key);
    } catch (UncheckedExecutionException e) {
      throw new DatasourceException(e.getMessage().split(": ", 2)[1]);
    }
    return result;
  }

  public long getCacheSize(){
    return cache.size();
  }
}
