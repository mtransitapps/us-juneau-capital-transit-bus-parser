package org.mtransit.parser.us_juneau_capital_transit_bus;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mtransit.commons.CharUtils;
import org.mtransit.commons.CleanUtils;
import org.mtransit.parser.DefaultAgencyTools;
import org.mtransit.parser.MTLog;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

// http://data.trilliumtransit.com/gtfs/cityandboroughofjuneau-ak-us/
// http://data.trilliumtransit.com/gtfs/cityandboroughofjuneau-ak-us/cityandboroughofjuneau-ak-us.zip
public class JuneauCapitalTransitBusAgencyTools extends DefaultAgencyTools {

	public static void main(@Nullable String[] args) {
		if (args == null || args.length == 0) {
			args = new String[3];
			args[0] = "input/gtfs.zip";
			args[1] = "../../mtransitapps/us-juneau-capital-transit-bus-android/res/raw/";
			args[2] = ""; // files-prefix
		}
		new JuneauCapitalTransitBusAgencyTools().start(args);
	}

	@Nullable
	private HashSet<Integer> serviceIdInts;

	@Override
	public void start(@NotNull String[] args) {
		MTLog.log("Generating Capital Transit bus data...");
		long start = System.currentTimeMillis();
		this.serviceIdInts = extractUsefulServiceIdInts(args, this, true);
		super.start(args);
		MTLog.log("Generating Capital Transit bus data... DONE in %s.", Utils.getPrettyDuration(System.currentTimeMillis() - start));
	}

	@Override
	public boolean excludingAll() {
		return this.serviceIdInts != null && this.serviceIdInts.isEmpty();
	}

	@Override
	public boolean excludeCalendar(@NotNull GCalendar gCalendar) {
		if (this.serviceIdInts != null) {
			return excludeUselessCalendarInt(gCalendar, this.serviceIdInts);
		}
		return super.excludeCalendar(gCalendar);
	}

	@Override
	public boolean excludeCalendarDate(@NotNull GCalendarDate gCalendarDates) {
		if (this.serviceIdInts != null) {
			return excludeUselessCalendarDateInt(gCalendarDates, this.serviceIdInts);
		}
		return super.excludeCalendarDate(gCalendarDates);
	}

	@Override
	public boolean excludeTrip(@NotNull GTrip gTrip) {
		if (this.serviceIdInts != null) {
			return excludeUselessTripInt(gTrip, this.serviceIdInts);
		}
		return super.excludeTrip(gTrip);
	}

	@NotNull
	@Override
	public Integer getAgencyRouteType() {
		return MAgency.ROUTE_TYPE_BUS;
	}

	@Override
	public long getRouteId(@NotNull GRoute gRoute) {
		return Long.parseLong(gRoute.getRouteShortName()); // using route short name as route ID
	}

	private static final String AGENCY_COLOR_BLUE = "000080"; // BLUE (from web site CSS)

	private static final String AGENCY_COLOR = AGENCY_COLOR_BLUE;

	@NotNull
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

	private static final HashMap<Long, RouteTripSpec> ALL_ROUTE_TRIPS2;

	static {
		HashMap<Long, RouteTripSpec> map2 = new HashMap<>();
		//noinspection deprecation
		map2.put(1L, new RouteTripSpec(1L, //
				0, MTrip.HEADSIGN_TYPE_STRING, DOUGLAS, //
				1, MTrip.HEADSIGN_TYPE_STRING, DOWNTOWN_TRANSIT_CENTER) // JUNEAU
				.addTripSort(0, //
						Arrays.asList(//
								"811771", // Downtown Transit Center
								"811672", // Glacier Avenue and 9th Street (Federal Building)
								"811789", // ==
								"811790", // != <>
								"811791", // ==
								"811821" // Savikko Road at Treadwell Ice Arena
						)) //
				.addTripSort(1, //
						Arrays.asList(//
								"811821", // Savikko Road at Treadwell Ice Arena
								"811810", // ==
								"811790", // != <>
								"811811", // ==
								"811771" // Downtown Transit Center
						)) //
				.compileBothTripSort());
		//noinspection deprecation
		map2.put(3L, new RouteTripSpec(3L, //
				0, MTrip.HEADSIGN_TYPE_STRING, MENDENHALL, //
				1, MTrip.HEADSIGN_TYPE_STRING, DOWNTOWN_TRANSIT_CENTER) // JUNEAU
				.addTripSort(0, //
						Arrays.asList(//
								"811771", // Downtown Transit Center
								"811687", // ==
								"811688", // != <>
								"811691", // != <>
								"811692", // ==
								"811701", // Mendenhall Mall Road and Riverside Drive (Mendenhall Mall)
								"811717" // Mendenhall Loop Road and Auke Lake Way (UAS)
						)) //
				.addTripSort(1, //
						Arrays.asList(//
								"811717", // Mendenhall Loop Road and Auke Lake Way (UAS)
								"811816", // Mallard Street and Crest Street (Nugget Mall Town)
								"811754", // ==
								"839422", // !=
								"811688", // != <>
								"811691", // != <>
								"811755", // ==
								"811771" // Downtown Transit Center
						)) //
				.compileBothTripSort());
		//noinspection deprecation
		map2.put(4L, new RouteTripSpec(4L, //
				0, MTrip.HEADSIGN_TYPE_STRING, MENDENHALL, //
				1, MTrip.HEADSIGN_TYPE_STRING, DOWNTOWN_TRANSIT_CENTER) // JUNEAU
				.addTripSort(0, //
						Arrays.asList(//
								"811771", // Downtown Transit Center
								"811687", // ==
								"811688", // != <>
								"811691", // != <>
								"811692", // ==
								"811731", // Glacier Highway at Auke Bay (Deharts)
								"811749" // Mendenhall Loop Road and Del Rae Road (Skate Park)
						)) //
				.addTripSort(1, //
						Arrays.asList(//
								"811749", // Mendenhall Loop Road and Del Rae Road (Skate Park)
								"811816", // Mallard Street and Crest Street (Nugget Mall Town)
								"811754", // ==
								"839422", // !=
								"811688", // != <>
								"811691", // != <>
								"811755", // ==
								"811771" // Downtown Transit Center
						)) //
				.compileBothTripSort());
		//noinspection deprecation
		map2.put(5L, new RouteTripSpec(5L, //
				0, MTrip.HEADSIGN_TYPE_STRING, UNIVERSITY, //
				1, MTrip.HEADSIGN_TYPE_STRING, DOWNTOWN) // JUNEAU
				.addTripSort(0, //
						Arrays.asList(//
								Stops.getALL_STOPS().get("540"), // 12th Street and Egan Drive (Mountain View Senior Center)
								Stops.getALL_STOPS().get("430"), // ++ Mendenhall Loop Road and Del Rae Road (Skate Park)
								Stops.getALL_STOPS().get("541") // Auke Lake Way (University of Alaska)
						)) //
				.addTripSort(1, //
						Arrays.asList(//
								Stops.getALL_STOPS().get("541"), // Auke Lake Way (University of Alaska)
								Stops.getALL_STOPS().get("451"), // ++ Glacier Highway and Sherwood Lane (DMV)
								Stops.getALL_STOPS().get("540") // 12th Street and Egan Drive (Mountain View Senior Center)
						)) //
				.compileBothTripSort());
		//noinspection deprecation
		map2.put(6L, new RouteTripSpec(6L, //
				0, MTrip.HEADSIGN_TYPE_STRING, RIVERSIDE, //
				1, MTrip.HEADSIGN_TYPE_STRING, DOWNTOWN) // JUNEAU
				.addTripSort(0, //
						Arrays.asList(//
								Stops.getALL_STOPS().get("540"), // 12th Street and Egan Drive (Mountain View Senior Center) #JUNEAU
								Stops.getALL_STOPS().get("576"), // Willoughby Avenue and Egan Drive (Centennial Hall) #JUNEAU
								Stops.getALL_STOPS().get("200"), // == Mallard Street and Crest Street (Nugget Mall Valley) #RIVERSIDE
								Stops.getALL_STOPS().get("542"), // <> Shell Simmons Drive at Juneau International Airport #RIVERSIDE
								Stops.getALL_STOPS().get("429"), // == Glacier Highway at Valley Restaurant
								Stops.getALL_STOPS().get("549") // Stephen Richards Memorial Drive and Coho Drive =>
						)) //
				.addTripSort(1, //
						Arrays.asList(//
								Stops.getALL_STOPS().get("549"), // Stephen Richards Memorial Drive and Coho Drive <=
								Stops.getALL_STOPS().get("453"), // == Glacier Highway and Berners Avenue (Professional Plaza)
								Stops.getALL_STOPS().get("542"), // <> Shell Simmons Drive at Juneau International Airport #RIVERSIDE
								Stops.getALL_STOPS().get("100"), // == Mallard Street and Crest Street (Nugget Mall Town) #RIVERSIDE
								Stops.getALL_STOPS().get("540") // 12th Street and Egan Drive (Mountain View Senior Center) #JUNEAU
						)) //
				.compileBothTripSort());
		//noinspection deprecation
		map2.put(7L, new RouteTripSpec(7L, //
				0, MTrip.HEADSIGN_TYPE_STRING, "", //
				1, MTrip.HEADSIGN_TYPE_STRING, DOWNTOWN_JUNEAU) //
				.addTripSort(0, //
						Collections.emptyList()) //
				.addTripSort(1, //
						Arrays.asList(//
								"811750", // Old Dairy Road and Glacier Highway (Fred Meyer)
								"811770" // 4th Street and Seward Street
						)) //
				.compileBothTripSort());
		//noinspection deprecation
		map2.put(8L, new RouteTripSpec(8L, //
				0, MTrip.HEADSIGN_TYPE_STRING, AUKE_BAY, // Valley
				1, MTrip.HEADSIGN_TYPE_STRING, "") //
				.addTripSort(0, //
						Arrays.asList(//
								"811768", // Marine Way at Downtown Library
								"811673", // ==
								"811674", // !=
								"811675", // !=
								"811676", // !=
								"811686", // !=
								"811687", // ==
								"811717" // Mendenhall Loop Road and Auke Lake Way (UAS)
						)) //
				.addTripSort(1, //
						Collections.emptyList()) //
				.compileBothTripSort());
		//noinspection deprecation
		map2.put(9L, new RouteTripSpec(9L, //
				0, MTrip.HEADSIGN_TYPE_STRING, "", //
				1, MTrip.HEADSIGN_TYPE_STRING, DOWNTOWN_JUNEAU) //
				.addTripSort(0, //
						Collections.emptyList()) //
				.addTripSort(1, //
						Arrays.asList(//
								"811731", // Glacier Highway at Auke Bay (Deharts)
								"811769" // Franklin St and Front Street (Pocket Park)
						)) //
				.compileBothTripSort());
		//noinspection deprecation
		map2.put(10L, new RouteTripSpec(10L, //
				0, MTrip.HEADSIGN_TYPE_STRING, "", //
				1, MTrip.HEADSIGN_TYPE_STRING, DOWNTOWN_JUNEAU) //
				.addTripSort(0, //
						Collections.emptyList()) //
				.addTripSort(1, //
						Arrays.asList(//
								"811732", // Mendenhall Loop Road and Auke Lake Way (UAS)
								"811771" // Downtown Transit Center
						)) //
				.compileBothTripSort());
		//noinspection deprecation
		map2.put(14L, new RouteTripSpec(14L, //
				0, MTrip.HEADSIGN_TYPE_STRING, "", //
				1, MTrip.HEADSIGN_TYPE_STRING, DOWNTOWN_TRANSIT_CENTER) //
				.addTripSort(0, //
						Collections.emptyList()) //
				.addTripSort(1, //
						Arrays.asList(//
								"811722", // Glacier Highway and Industrial Boulevard (Job Center)
								"811771" // Downtown Transit Center
						)) //
				.compileBothTripSort());
		//noinspection deprecation
		map2.put(15L, new RouteTripSpec(15L, //
				0, MTrip.HEADSIGN_TYPE_STRING, "", //
				1, MTrip.HEADSIGN_TYPE_STRING, AUKE_BAY) //
				.addTripSort(0, //
						Collections.emptyList()) //
				.addTripSort(1, //
						Arrays.asList(//
								"811771", // Downtown Transit Center
								"811813" // Auke Lake Way (University of Alaska)
						)) //
				.compileBothTripSort());
		//noinspection deprecation
		map2.put(16L, new RouteTripSpec(16L, //
				0, MTrip.HEADSIGN_TYPE_STRING, "", //
				1, MTrip.HEADSIGN_TYPE_STRING, DOWNTOWN_TRANSIT_CENTER) // JUNEAU
				.addTripSort(0, //
						Collections.emptyList()) //
				.addTripSort(1, //
						Arrays.asList(//
								"811722", // Glacier Highway and Industrial Boulevard (Job Center)
								"811771" // Downtown Transit Center
						)) //
				.compileBothTripSort());
		ALL_ROUTE_TRIPS2 = map2;
	}

	@Override
	public int compareEarly(long routeId, @NotNull List<MTripStop> list1, @NotNull List<MTripStop> list2, @NotNull MTripStop ts1, @NotNull MTripStop ts2, @NotNull GStop ts1GStop, @NotNull GStop ts2GStop) {
		if (ALL_ROUTE_TRIPS2.containsKey(routeId)) {
			return ALL_ROUTE_TRIPS2.get(routeId).compare(routeId, list1, list2, ts1, ts2, ts1GStop, ts2GStop, this);
		}
		return super.compareEarly(routeId, list1, list2, ts1, ts2, ts1GStop, ts2GStop);
	}

	@NotNull
	@Override
	public ArrayList<MTrip> splitTrip(@NotNull MRoute mRoute, @Nullable GTrip gTrip, @NotNull GSpec gtfs) {
		if (ALL_ROUTE_TRIPS2.containsKey(mRoute.getId())) {
			return ALL_ROUTE_TRIPS2.get(mRoute.getId()).getAllTrips();
		}
		return super.splitTrip(mRoute, gTrip, gtfs);
	}

	@NotNull
	@Override
	public Pair<Long[], Integer[]> splitTripStop(@NotNull MRoute mRoute, @NotNull GTrip gTrip, @NotNull GTripStop gTripStop, @NotNull ArrayList<MTrip> splitTrips, @NotNull GSpec routeGTFS) {
		if (ALL_ROUTE_TRIPS2.containsKey(mRoute.getId())) {
			return SplitUtils.splitTripStop(mRoute, gTrip, gTripStop, routeGTFS, ALL_ROUTE_TRIPS2.get(mRoute.getId()), this);
		}
		return super.splitTripStop(mRoute, gTrip, gTripStop, splitTrips, routeGTFS);
	}

	@Override
	public void setTripHeadsign(@NotNull MRoute mRoute, @NotNull MTrip mTrip, @NotNull GTrip gTrip, @NotNull GSpec gtfs) {
		if (ALL_ROUTE_TRIPS2.containsKey(mRoute.getId())) {
			return; // split
		}
		if (gTrip.getDirectionId() == null) {
			throw new MTLog.Fatal("%s: Unexpected trip %s!", mRoute.getId(), gTrip);
		}
		mTrip.setHeadsignString(
				cleanTripHeadsign(gTrip.getTripHeadsignOrDefault()),
				gTrip.getDirectionIdOrDefault()
		);
	}

	@Override
	public boolean mergeHeadsign(@NotNull MTrip mTrip, @NotNull MTrip mTripToMerge) {
		throw new MTLog.Fatal("Unexpected trips to merge: %s & %s!", mTrip, mTripToMerge);
	}

	@NotNull
	@Override
	public String cleanTripHeadsign(@NotNull String tripHeadsign) {
		tripHeadsign = CleanUtils.cleanSlashes(tripHeadsign);
		tripHeadsign = CleanUtils.cleanNumbers(tripHeadsign);
		tripHeadsign = CleanUtils.cleanStreetTypes(tripHeadsign);
		return CleanUtils.cleanLabel(tripHeadsign);
	}

	@NotNull
	@Override
	public String cleanStopName(@NotNull String gStopName) {
		gStopName = CleanUtils.SAINT.matcher(gStopName).replaceAll(CleanUtils.SAINT_REPLACEMENT);
		gStopName = CleanUtils.CLEAN_AND.matcher(gStopName).replaceAll(CleanUtils.CLEAN_AND_REPLACEMENT);
		gStopName = CleanUtils.CLEAN_AT.matcher(gStopName).replaceAll(CleanUtils.CLEAN_AT_REPLACEMENT);
		gStopName = CleanUtils.cleanSlashes(gStopName);
		gStopName = CleanUtils.cleanStreetTypes(gStopName);
		return CleanUtils.cleanLabel(gStopName);
	}

	@Override
	public int getStopId(@NotNull GStop gStop) {
		if (gStop.getStopCode().isEmpty() || //
				!CharUtils.isDigitsOnly(gStop.getStopCode())) {
			//noinspection deprecation
			final String stopId = gStop.getStopId();
			if ("2555716".equals(stopId)) {
				return 401; // Willoughby Avenue and Egan Drive (Centennial Hall)
			}
			if ("2555715".equals(stopId)) {
				return 402; // Willoughby Avenue and Whittier Way (ANB Hall)
			}
			throw new MTLog.Fatal("Unexpected stop ID for %s!", gStop);
		}
		return Integer.parseInt(gStop.getStopCode()); // use stop code as stop ID
	}
}
