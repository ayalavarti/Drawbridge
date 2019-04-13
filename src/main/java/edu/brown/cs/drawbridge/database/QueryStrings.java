package edu.brown.cs.drawbridge.database;

public final class QueryStrings {

  protected static final String INSERT_USER = "INSERT INTO users VALUES (?, ?, ?);";
  protected static final String INSERT_TRIP = "INSERT INTO trips(name, start_name,"
          + "start_latitude, start_longitude, end_name, end_latitude, end_longitude, "
          + "departure, eta, max_people, total_cost, phone_number, transportation, "
          + "description) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

  protected static final String REMOVE_TRIP_BY_ID = "DELETE FROM trips WHERE id = ?;";
  protected static final String REMOVE_TRIPS_BY_TIME = "DELETE FROM trips WHERE "
          + "departure < EXTRACT(EPOCH FROM current_timestamp);";
  protected static final String INSERT_HOST = "INSERT INTO hosts(trip_id, user_id) VALUES (?, ?);";
  protected static final String INSERT_MEMBER = "INSERT INTO members(trip_id, user_id) VALUES (?, ?);"; //"INSERT INTO members(trip_id, user_id, group_id) VALUES (?, ?, ?);";
  protected static final String INSERT_REQUEST = "INSERT INTO requests(trip_id, user_id) VALUES (?, ?);"; //"INSERT INTO requests(trip_id, user_id, group_id) VALUES (?, ?, ?);";
  protected static final String REMOVE_MEMBER = "DELETE FROM members WHERE trip_id = ? AND user_id = ?;"; //"DELETE FROM members WHERE trip_id = ? AND user_id = ? RETURNING group_id;";
  protected static final String REMOVE_REQUEST = "DELETE FROM requests WHERE trip_id = ? AND user_id = ?;"; //"DELETE FROM requests WHERE trip_id = ? AND user_id = ? RETURNING group_id;";
  protected static final String REMOVE_MEMBER_GROUP = "DELETE FROM members WHERE group_id = ?";
  protected static final String REMOVE_REQUEST_GROUP = "DELETE FROM requests WHERE group_id = ?";

  protected static final String FIND_USER_BY_ID = "SELECT * FROM users WHERE id = ?;";
  protected static final String FIND_TRIP_BY_ID = "SELECT * FROM trips WHERE id = ?;";
  protected static final String FIND_HOST_TRIPS = "SELECT trip_id FROM hosts WHERE user_id = ?;";
  protected static final String FIND_MEMBER_TRIPS = "SELECT trip_id FROM members WHERE user_id = ?;"; //"SELECT trip_id FROM members WHERE user_id = ? GROUP BY group_id;";
  protected static final String FIND_REQUEST_TRIPS = "SELECT trip_id FROM requests WHERE user_id = ?;"; //"SELECT trip_id FROM requests WHERE user_id = ? GROUP BY group_id;";
  protected static final String FIND_HOST_USER = "SELECT user_id FROM hosts WHERE trip_id = ?;";
  protected static final String FIND_MEMBER_USERS = "SELECT user_id FROM members WHERE trip_id = ?;"; //"SELECT user_id FROM members WHERE trip_id = ? GROUP BY group_id;";
  protected static final String FIND_REQUEST_USERS = "SELECT user_id FROM requests WHERE trip_id = ?;"; //"SELECT user_id FROM requests WHERE trip_id = ? GROUP BY group_id;";
  protected static final String FIND_GROUP_TRIP = "SELECT trip_id FROM (SELECT * FROM members UNION SELECT * FROM requests) WHERE group_id = ?;";

  protected static final String FIND_CONNECTED_TRIPS = "SELECT * FROM trips WHERE "
          + "(degrees(2 * asin(sqrt(sin(radians(? - start_latitude) / 2)^2 + "
          + "cos(radians(start_latitude)) * cos(radians(?)) * sin(radians(? - start_longitude) / 2)^2))) <= ?) "
          + "AND (departure BETWEEN ? AND ?);";
  //lat2, lat2, lon2, walkradius, start, end
}
