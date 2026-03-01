package pl.manager.library.database;

import java.util.Map;

public interface IStatisticsRepository {
    Map<String, Integer> getStatistics();
    Map<String, Integer> getMostPopularBooks(int limit);
}