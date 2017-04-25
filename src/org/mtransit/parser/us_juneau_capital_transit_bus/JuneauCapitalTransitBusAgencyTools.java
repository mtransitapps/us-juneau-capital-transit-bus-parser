package org.mtransit.parser.us_juneau_capital_transit_bus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;

import org.mtransit.parser.CleanUtils;
import org.mtransit.parser.DefaultAgencyTools;
import org.mtransit.parser.Pair;
import org.mtransit.parser.SplitUtils;
import org.mtransit.parser.SplitUtils.RouteTripSpec;
import org.mtransit.parser.Utils;
import org.mtransit.parser.gtfs.data.GCalendar;
import org.mtransit.parser.gtfs.data.GCalendarDate;
import org.mtransit.parser.gtfs.data.GRoute;
import org.mtransit.parser.gtfs.data.GSpec;
import org.mtransit.parser.gtfs.data.GStop;
import org.mtransit.parser.gtfs.data.GTrip;
import org.mtransit.parser.gtfs.data.GTripStop;
import org.mtransit.parser.mt.data.MAgency;
import org.mtransit.parser.mt.data.MRoute;
import org.mtransit.parser.mt.data.MTrip;
import org.mtransit.parser.mt.data.MTripStop;

// http://data.trilliumtransit.com/gtfs/cityandboroughofjuneau-ak-us/
// http://data.trilliumtransit.com/gtfs/cityandboroughofjuneau-ak-us/cityandboroughofjuneau-ak-us.zip
public class JuneauCapitalTransitBusAgencyTools extends DefaultAgencyTools {

	public static void main(String[] args) {
		if (args == null || args.length == 0) {
			args = new String[3];
			args[0] = "input/gtfs.zip";
			args[1] = "../../mtransitapps/us-juneau-capital-transit-bus-android/res/raw/";
			args[2] = ""; // files-prefix
		}
		new JuneauCapitalTransitBusAgencyTools().start(args);
	}

	private HashSet<String> serviceIds;

	@Override
	public void start(String[] args) {
		System.out.printf("\nGenerating Capital Transit bus data...");
		long start = System.currentTimeMillis();
		this.serviceIds = extractUsefulServiceIds(args, this, true);
		super.start(args);
		System.out.printf("\nGenerating Capital Transit bus data... DONE in %s.\n", Utils.getPrettyDuration(System.currentTimeMillis() - start));
	}

	@Override
	public boolean excludeCalendar(GCalendar gCalendar) {
		if (this.serviceIds != null) {
			return excludeUselessCalendar(gCalendar, this.serviceIds);
		}
		return super.excludeCalendar(gCalendar);
	}

	@Override
	public boolean excludeCalendarDate(GCalendarDate gCalendarDates) {
		if (this.serviceIds != null) {
			return excludeUselessCalendarDate(gCalendarDates, this.serviceIds);
		}
		return super.excludeCalendarDate(gCalendarDates);
	}

	@Override
	public boolean excludeTrip(GTrip gTrip) {
		if (this.serviceIds != null) {
			return excludeUselessTrip(gTrip, this.serviceIds);
		}
		return super.excludeTrip(gTrip);
	}

	@Override
	public Integer getAgencyRouteType() {
		return MAgency.ROUTE_TYPE_BUS;
	}

	@Override
	public long getRouteId(GRoute gRoute) {
		return Long.parseLong(gRoute.getRouteShortName()); // using route short name as route ID
	}

	private static final String AGENCY_COLOR_BLUE = "000080"; // BLUE (from web site CSS)

	private static final String AGENCY_COLOR = AGENCY_COLOR_BLUE;

	@Override
	public String getAgencyColor() {
		return AGENCY_COLOR;
	}

	private static final String TRANSIT_CENTER_SHORT = "TC";
	private static final String AUKE_BAY = "Auke Bay";
	private static final String DOUGLAS = "Douglas";
	private static final String DOWNTOWN = "Downtown";
	private static final String JUNEAU = "Juneau";
	private static final String MENDENHALL = "Mendenhall";
	private static final String RIVERSIDE = "Riverside";
	private static final String UNIVERSITY = "University";
	private static final String DOWNTOWN_JUNEAU = DOWNTOWN + " " + JUNEAU;
	private static final String DOWNTOWN_TRANSIT_CENTER = DOWNTOWN + " " + TRANSIT_CENTER_SHORT;

	private static HashMap<Long, RouteTripSpec> ALL_ROUTE_TRIPS2;
	static {
		HashMap<Long, RouteTripSpec> map2 = new HashMap<Long, RouteTripSpec>();
		map2.put(1L, new RouteTripSpec(1L, //
				0, MTrip.HEADSIGN_TYPE_STRING, DOUGLAS, //
				1, MTrip.HEADSIGN_TYPE_STRING, DOWNTOWN_TRANSIT_CENTER) // JUNEAU
				.addTripSort(0, //
						Arrays.asList(new String[] { //
						"811771", // Downtown Transit Center
								"811672", // Glacier Avenue and 9th Street (Federal Building)
								"811789", // ==
								"811790", // != <>
								"811791", // ==
								"811821", // Savikko Road at Treadwell Ice Arena
						})) //
				.addTripSort(1, //
						Arrays.asList(new String[] { //
						"811821", // Savikko Road at Treadwell Ice Arena
								"811810", // ==
								"811790", // != <>
								"811811", // ==
								"811771", // Downtown Transit Center
						})) //
				.compileBothTripSort());
		map2.put(3L, new RouteTripSpec(3L, //
				0, MTrip.HEADSIGN_TYPE_STRING, MENDENHALL, //
				1, MTrip.HEADSIGN_TYPE_STRING, DOWNTOWN_TRANSIT_CENTER) // JUNEAU
				.addTripSort(0, //
						Arrays.asList(new String[] { //
						"811771", // Downtown Transit Center
								"811687", // ==
								"811688", // != <>
								"811691", // != <>
								"811692", // ==
								"811701", // Mendenhall Mall Road and Riverside Drive (Mendenhall Mall)
								"811717", // Mendenhall Loop Road and Auke Lake Way (UAS)
						})) //
				.addTripSort(1, //
						Arrays.asList(new String[] { //
						"811717", // Mendenhall Loop Road and Auke Lake Way (UAS)
								"811816", // Mallard Street and Crest Street (Nugget Mall Town)
								"811754", // ==
								"839422", // !=
								"811688", // != <>
								"811691", // != <>
								"811755", // ==
								"811771", // Downtown Transit Center
						})) //
				.compileBothTripSort());
		map2.put(4L, new RouteTripSpec(4L, //
				0, MTrip.HEADSIGN_TYPE_STRING, MENDENHALL, //
				1, MTrip.HEADSIGN_TYPE_STRING, DOWNTOWN_TRANSIT_CENTER) // JUNEAU
				.addTripSort(0, //
						Arrays.asList(new String[] { //
						"811771", // Downtown Transit Center
								"811687", // ==
								"811688", // != <>
								"811691", // != <>
								"811692", // ==
								"811731", // Glacier Highway at Auke Bay (Deharts)
								"811749", // Mendenhall Loop Road and Del Rae Road (Skate Park)
						})) //
				.addTripSort(1, //
						Arrays.asList(new String[] { //
						"811749", // Mendenhall Loop Road and Del Rae Road (Skate Park)
								"811816", // Mallard Street and Crest Street (Nugget Mall Town)
								"811754", // ==
								"839422", // !=
								"811688", // != <>
								"811691", // != <>
								"811755", // ==
								"811771", // Downtown Transit Center
						})) //
				.compileBothTripSort());
		map2.put(5L, new RouteTripSpec(5L, //
				0, MTrip.HEADSIGN_TYPE_STRING, UNIVERSITY, //
				1, MTrip.HEADSIGN_TYPE_STRING, DOWNTOWN) // JUNEAU
				.addTripSort(0, //
						Arrays.asList(new String[] { //
						"811669", // Willoughby Avenue and Egan Drive (Centennial Hall)
								"811700", // ++
								"811813", // Auke Lake Way (University of Alaska)
						})) //
				.addTripSort(1, //
						Arrays.asList(new String[] { //
						"811813", // Auke Lake Way (University of Alaska)
								"811721", // ++
								"811669", // Willoughby Avenue and Egan Drive (Centennial Hall)
						})) //
				.compileBothTripSort());
		map2.put(6L, new RouteTripSpec(6L, //
				0, MTrip.HEADSIGN_TYPE_STRING, RIVERSIDE, //
				1, MTrip.HEADSIGN_TYPE_STRING, DOWNTOWN) // JUNEAU
				.addTripSort(0, //
						Arrays.asList(new String[] { //
						"811669", // Willoughby Avenue and Egan Drive (Centennial Hall)
								"811672", // !=
								"811816", // Mallard Street and Crest Street (Nugget Mall Town)
								"811697", // Mallard Street and Crest Street (Nugget Mall Valley)
								"811815", // <>
								"811699", // ==
								"811824", // Stephen Richards Memorial Drive and Coho Drive
						})) //
				.addTripSort(1, //
						Arrays.asList(new String[] { //
						"811824", // Stephen Richards Memorial Drive and Coho Drive
								"811724", // ==
								"811815", // <>
								"811816", // Mallard Street and Crest Street (Nugget Mall Town)
								"811697", // Mallard Street and Crest Street (Nugget Mall Valley)
								"811669", // Willoughby Avenue and Egan Drive (Centennial Hall)
						})) //
				.compileBothTripSort());
		map2.put(7L, new RouteTripSpec(7L, //
				0, MTrip.HEADSIGN_TYPE_STRING, "", //
				1, MTrip.HEADSIGN_TYPE_STRING, DOWNTOWN_JUNEAU) //
				.addTripSort(0, //
						Arrays.asList(new String[] { //
						/** no stops **/
						})) //
				.addTripSort(1, //
						Arrays.asList(new String[] { //
						"811750", // Old Dairy Road and Glacier Highway (Fred Meyer)
								"811770", // 4th Street and Seward Street
						})) //
				.compileBothTripSort());
		map2.put(8L, new RouteTripSpec(8L, //
				0, MTrip.HEADSIGN_TYPE_STRING, AUKE_BAY, // Valley
				1, MTrip.HEADSIGN_TYPE_STRING, "") //
				.addTripSort(0, //
						Arrays.asList(new String[] { //
						"811768", // Marine Way at Downtown Library
								"811673", // ==
								"811674", // !=
								"811675", // !=
								"811676", // !=
								"811686", // !=
								"811687", // ==
								"811717", // Mendenhall Loop Road and Auke Lake Way (UAS)
						})) //
				.addTripSort(1, //
						Arrays.asList(new String[] { //
						/** no stops **/
						})) //
				.compileBothTripSort());
		map2.put(9L, new RouteTripSpec(9L, //
				0, MTrip.HEADSIGN_TYPE_STRING, "", //
				1, MTrip.HEADSIGN_TYPE_STRING, DOWNTOWN_JUNEAU) //
				.addTripSort(0, //
						Arrays.asList(new String[] { //
						/** no stops **/
						})) //
				.addTripSort(1, //
						Arrays.asList(new String[] { //
						"811731", // Glacier Highway at Auke Bay (Deharts)
								"811769", // Franklin St and Front Street (Pocket Park)
						})) //
				.compileBothTripSort());
		map2.put(10L, new RouteTripSpec(10L, //
				0, MTrip.HEADSIGN_TYPE_STRING, "", //
				1, MTrip.HEADSIGN_TYPE_STRING, DOWNTOWN_JUNEAU) //
				.addTripSort(0, //
						Arrays.asList(new String[] { //
						/** no stops **/
						})) //
				.addTripSort(1, //
						Arrays.asList(new String[] { //
						"811732", // Mendenhall Loop Road and Auke Lake Way (UAS)
								"811771", // Downtown Transit Center
						})) //
				.compileBothTripSort());
		map2.put(14L, new RouteTripSpec(14L, //
				0, MTrip.HEADSIGN_TYPE_STRING, "", //
				1, MTrip.HEADSIGN_TYPE_STRING, DOWNTOWN_TRANSIT_CENTER) //
				.addTripSort(0, //
						Arrays.asList(new String[] { //
						/** no stops **/
						})) //
				.addTripSort(1, //
						Arrays.asList(new String[] { //
						"811722", // Glacier Highway and Industrial Boulevard (Job Center)
								"811771", // Downtown Transit Center
						})) //
				.compileBothTripSort());
		map2.put(15L, new RouteTripSpec(15L, //
				0, MTrip.HEADSIGN_TYPE_STRING, "", //
				1, MTrip.HEADSIGN_TYPE_STRING, AUKE_BAY) //
				.addTripSort(0, //
						Arrays.asList(new String[] { //
						/** no stops **/
						})) //
				.addTripSort(1, //
						Arrays.asList(new String[] { //
						"811771", // Downtown Transit Center
								"811813", // Auke Lake Way (University of Alaska)
						})) //
				.compileBothTripSort());
		map2.put(16L, new RouteTripSpec(16L, //
				0, MTrip.HEADSIGN_TYPE_STRING, "", //
				1, MTrip.HEADSIGN_TYPE_STRING, DOWNTOWN_TRANSIT_CENTER) // JUNEAU
				.addTripSort(0, //
						Arrays.asList(new String[] { //
						/** no stops **/
						})) //
				.addTripSort(1, //
						Arrays.asList(new String[] { //
						"811722", // Glacier Highway and Industrial Boulevard (Job Center)
								"811771", // Downtown Transit Center
						})) //
				.compileBothTripSort());
		ALL_ROUTE_TRIPS2 = map2;
	}

	@Override
	public int compareEarly(long routeId, List<MTripStop> list1, List<MTripStop> list2, MTripStop ts1, MTripStop ts2, GStop ts1GStop, GStop ts2GStop) {
		if (ALL_ROUTE_TRIPS2.containsKey(routeId)) {
			return ALL_ROUTE_TRIPS2.get(routeId).compare(routeId, list1, list2, ts1, ts2, ts1GStop, ts2GStop);
		}
		return super.compareEarly(routeId, list1, list2, ts1, ts2, ts1GStop, ts2GStop);
	}

	@Override
	public ArrayList<MTrip> splitTrip(MRoute mRoute, GTrip gTrip, GSpec gtfs) {
		if (ALL_ROUTE_TRIPS2.containsKey(mRoute.getId())) {
			return ALL_ROUTE_TRIPS2.get(mRoute.getId()).getAllTrips();
		}
		return super.splitTrip(mRoute, gTrip, gtfs);
	}

	@Override
	public Pair<Long[], Integer[]> splitTripStop(MRoute mRoute, GTrip gTrip, GTripStop gTripStop, ArrayList<MTrip> splitTrips, GSpec routeGTFS) {
		if (ALL_ROUTE_TRIPS2.containsKey(mRoute.getId())) {
			return SplitUtils.splitTripStop(mRoute, gTrip, gTripStop, routeGTFS, ALL_ROUTE_TRIPS2.get(mRoute.getId()));
		}
		return super.splitTripStop(mRoute, gTrip, gTripStop, splitTrips, routeGTFS);
	}

	@Override
	public void setTripHeadsign(MRoute mRoute, MTrip mTrip, GTrip gTrip, GSpec gtfs) {
		if (ALL_ROUTE_TRIPS2.containsKey(mRoute.getId())) {
			return; // split
		}
		if (gTrip.getDirectionId() == null) {
			System.out.printf("\n%s: Unexpected trip %s!\n", mRoute.getId(), gTrip);
			System.exit(-1);
			return;
		}
		mTrip.setHeadsignString(cleanTripHeadsign(gTrip.getTripHeadsign()), gTrip.getDirectionId());
	}

	@Override
	public boolean mergeHeadsign(MTrip mTrip, MTrip mTripToMerge) {
		System.out.printf("\nUnexpected trips to merge: %s & %s!\n", mTrip, mTripToMerge);
		System.exit(-1);
		return false;
	}

	private static final Pattern TRANSIT_CENTER = Pattern.compile("((^|\\W){1}(transit center|transit|centre)(\\W|$){1})", Pattern.CASE_INSENSITIVE);
	private static final String TRANSIT_CENTER_REPLACEMENT = "$2" + TRANSIT_CENTER_SHORT + "$4";

	@Override
	public String cleanTripHeadsign(String tripHeadsign) {
		tripHeadsign = TRANSIT_CENTER.matcher(tripHeadsign).replaceAll(TRANSIT_CENTER_REPLACEMENT);
		tripHeadsign = CleanUtils.cleanSlashes(tripHeadsign);
		tripHeadsign = CleanUtils.cleanNumbers(tripHeadsign);
		tripHeadsign = CleanUtils.cleanStreetTypes(tripHeadsign);
		return CleanUtils.cleanLabel(tripHeadsign);
	}

	@Override
	public String cleanStopName(String gStopName) {
		gStopName = CleanUtils.SAINT.matcher(gStopName).replaceAll(CleanUtils.SAINT_REPLACEMENT);
		gStopName = CleanUtils.CLEAN_AND.matcher(gStopName).replaceAll(CleanUtils.CLEAN_AND_REPLACEMENT);
		gStopName = CleanUtils.CLEAN_AT.matcher(gStopName).replaceAll(CleanUtils.CLEAN_AT_REPLACEMENT);
		gStopName = CleanUtils.cleanSlashes(gStopName);
		gStopName = CleanUtils.removePoints(gStopName);
		gStopName = CleanUtils.cleanStreetTypes(gStopName);
		return CleanUtils.cleanLabel(gStopName);
	}

	@Override
	public int getStopId(GStop gStop) {
		return Integer.parseInt(gStop.getStopCode()); // use stop code as stop ID
	}
}
