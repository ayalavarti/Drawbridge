package edu.brown.cs.drawbridge.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import edu.brown.cs.drawbridge.models.Trip;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class JSONProcessorTest {

  private static final Trip t1 = Trip.TripBuilder.newTripBuilder()
         .addIdentification(1, "First trip").addLocations(3, 3, 5, 5)
         .addAddressNames("(3, 3)", "(5, 5)").addTimes(1000, 1500)
         .addDetails(5, 20.00, "555-555-5555", "car", "")
         .buildWithUsers("abcdef", new ArrayList<>(), new ArrayList<>());
  private static final Trip t2 = Trip.TripBuilder.newTripBuilder()
         .addIdentification(2, "Second trip").addLocations(0, 0, 5, 4)
         .addAddressNames("(0, 0)", "(5, 4)").addTimes(900, 2000)
         .addDetails(4, 15.50, "444-444-4444", "car", "")
         .buildWithUsers("dasfasd", new ArrayList<>(), new ArrayList<>());
  private static final Trip t3 = Trip.TripBuilder.newTripBuilder()
         .addIdentification(3, "Third trip").addLocations(2, 3, 9, 9)
         .addAddressNames("(2, 3)", "(9, 9)").addTimes(500, 1500)
         .addDetails(3, 16.00, "333-333-3333", "car", "")
         .buildWithUsers("whodunnit", new ArrayList<>(), new ArrayList<>());

  @Test
  public void testProcessTripGroups() {
    List<Trip> g1 = new ArrayList<>();
    g1.add(t1);
    g1.add(t2);

    List<Trip> g2 = new ArrayList<>();
    g2.add(t3);

    List<List<Trip>> groups = new ArrayList<>();
    groups.add(g1);
    groups.add(g2);

    JsonArray json = JSONProcessor.processTripGroups(groups);
    JsonArray jtg1 = (JsonArray) json.get(0);
    JsonArray jtg2 = (JsonArray) json.get(1);

    assert jtg1.size() == 2;
    assert jtg2.size() == 1;

    JsonObject jt1 = (JsonObject) jtg1.get(0);
    JsonObject jt2 = (JsonObject) jtg1.get(1);
    JsonObject jt3 = (JsonObject) jtg2.get(0);

    assert jt1.equals(t1.toJson());
    assert jt2.equals(t2.toJson());
    assert jt3.equals(t3.toJson());
  }

  @Test
  public void testProcessNoGroups() {
    JsonArray json = JSONProcessor.processTripGroups(new ArrayList<>());
    assert json.size() == 0;
  }

  @Test
  public void testProcessEmptyGroup() {
    List<Trip> emptyGroup = new ArrayList<>();
    List<List<Trip>> data = new ArrayList<>();
    data.add(emptyGroup);

    JsonArray json = JSONProcessor.processTripGroups(data);
    assert json.size() == 1;

    JsonArray jgt1 = (JsonArray) json.get(0);
    assert jgt1.size() == 0;
  }

  @Test
  public void testProcessTripGroupsWithUID() {
    String uid = "abcdefgzxy";

    List<Trip> g1 = new ArrayList<>();
    g1.add(t1);
    g1.add(t2);

    List<Trip> g2 = new ArrayList<>();
    g2.add(t3);

    List<List<Trip>> groups = new ArrayList<>();
    groups.add(g1);
    groups.add(g2);

    JsonArray json = JSONProcessor.processTripGroups(uid, groups);
    JsonArray jtg1 = (JsonArray) json.get(0);
    JsonArray jtg2 = (JsonArray) json.get(1);

    assert jtg1.size() == 2;
    assert jtg2.size() == 1;

    JsonObject jt1 = (JsonObject) jtg1.get(0);
    JsonObject jt2 = (JsonObject) jtg1.get(1);
    JsonObject jt3 = (JsonObject) jtg2.get(0);

    assert jt1.equals(t1.toJson(uid));
    assert jt2.equals(t2.toJson(uid));
    assert jt3.equals(t3.toJson(uid));
  }
}
