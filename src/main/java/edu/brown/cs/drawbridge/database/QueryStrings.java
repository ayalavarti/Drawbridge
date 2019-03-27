package edu.brown.cs.drawbridge.database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
  protected static final String INSERT_MEMBER = "INSERT INTO members(trip_id, user_id) VALUES (?, ?);";
  protected static final String INSERT_REQUEST = "INSERT INTO requests(trip_id, user_id) VALUES (?, ?);";
  protected static final String REMOVE_MEMBER = "DELETE FROM members WHERE trip_id = ? AND user_id = ?;";
  protected static final String REMOVE_REQUEST = "DELETE FROM requests WHERE trip_id = ? AND user_id = ?;";

  protected static final String FIND_USER_BY_ID = "SELECT * FROM users WHERE id = ?;";
  protected static final String FIND_TRIP_BY_ID = "SELECT * FROM trips WHERE id = ?;";
  protected static final String FIND_HOST_TRIPS = "SELECT trip_id FROM hosts WHERE user_id = ?";
  protected static final String FIND_MEMBER_TRIPS = "SELECT trip_id FROM members WHERE user_id = ?";
  protected static final String FIND_REQUEST_TRIPS = "SELECT trip_id FROM requests WHERE user_id = ?";
  protected static final String FIND_HOST_USER = "SELECT user_id FROM hosts WHERE trip_id = ?";
  protected static final String FIND_MEMBER_USERS = "SELECT user_id FROM members WHERE trip_id = ?";
  protected static final String FIND_REQUEST_USERS = "SELECT user_id FROM requests WHERE trip_id = ?";

  protected static final String FIND_CONNECTED_TRIPS = "SELECT * FROM trips WHERE "
          + "(degrees(2 * asin(sqrt(sin((? - start_latitude) / 2)^2 + "
          + "cos(start_latitude) * cos(?) * sin((? - start_longitude) / 2)^2)) < ?) "
          + "AND (departure BETWEEN ? AND ?);";
  //lat2, lat2, lon2, walkradius, start, end

  protected static final String SETUP_USERS = "CREATE IF NOT EXISTS users("
          + "id TEXT NOT NULL PRIMARY KEY, "
          + "name TEXT NOT NULL, "
          + "email TEXT NOT NULL);";
  protected static final String SETUP_TRIPS = "CREATE IF NOT EXISTS trips("
          + "id SERIAL NOT NULL PRIMARY KEY, "
          + "name TEXT NOT NULL, "
          + "start_name TEXT, "
          + "start_latitude DOUBLE PRECISION NOT NULL, "
          + "start_longitude DOUBLE PRECISION NOT NULL, "
          + "end_name TEXT, "
          + "end_latitude DOUBLE PRECISION NOT NULL, "
          + "end_longitude DOUBLE PRECISION NOT NULL, "
          + "departure BIGINT NOT NULL, "
          + "eta BIGINT NOT NULL, "
          + "max_people SMALLINT NOT NULL, "
          + "total_cost REAL NOT NULL, "
          + "phone_number TEXT NOT NULL, "
          + "transportation TEXT NOT NULL, "
          + "description TEXT NOT NULL);";
  protected static final String SETUP_HOSTS = "CREATE IF NOT EXISTS hosts("
          + "user_id TEXT NOT NULL REFERENCES users.id ON DELETE CASCADE, "
          + "trip_id INTEGER NOT NULL REFERENCES trips.id ON DELETE CASCADE);";
  protected static final String SETUP_MEMBERS = "CREATE IF NOT EXISTS members("
          + "user_id TEXT NOT NULL REFERENCES users.id ON DELETE CASCADE, "
          + "trip_id INTEGER NOT NULL REFERENCES trips.id ON DELETE CASCADE);";
  protected static final String SETUP_REQUESTS = "CREATE IF NOT EXISTS requests("
          + "user_id TEXT NOT NULL REFERENCES users.id ON DELETE CASCADE, "
          + "trip_id INTEGER NOT NULL REFERENCES trips.id ON DELETE CASCADE);";
  protected static final List<String> SETUP_QUERIES = new ArrayList<String>(
          Arrays.asList(SETUP_USERS, SETUP_TRIPS, SETUP_HOSTS, SETUP_MEMBERS,
                  SETUP_REQUESTS));
}
