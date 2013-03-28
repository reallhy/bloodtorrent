package org.bloodtorrent.resources;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.bloodtorrent.BloodTorrentConstants;
import org.bloodtorrent.IllegalDataException;
import org.bloodtorrent.dto.BloodRequest;
import org.bloodtorrent.dto.User;
import org.bloodtorrent.repository.UsersRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sds
 * Date: 13. 3. 26
 * Time: 오전 10:56
 * To change this template use File | Settings | File Templates.
 */
public class FindingMatchingDonorResource implements BloodTorrentConstants {
    private final double STATUTE_MILE_TO_KILLOMETER = 1.609344;
    private final double NAUTICAL_MILE_TO_STATUTE_MILE = 1.1515;

    private UsersRepository usersRepository;

    public FindingMatchingDonorResource(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }



    public double getDistance(double latitudeOfDonor, double longitudeOfDonor, double latitudeOfHospital, double longitudeOfHospital) {
        double theta = longitudeOfDonor - longitudeOfHospital;
        double distance = Math.sin(degreeToRadian(latitudeOfDonor)) * Math.sin(degreeToRadian(latitudeOfHospital)) + Math.cos(degreeToRadian(latitudeOfDonor)) * Math.cos(degreeToRadian(latitudeOfHospital)) * Math.cos(degreeToRadian(theta));
        distance = Math.acos(distance);
        distance = radianToDegree(distance);

        distance = distanceToKillometer(distance);
        return distance;
    }

    private double distanceToKillometer(double distance) {
        return NauticalMilesToStatuteMile(distance) * STATUTE_MILE_TO_KILLOMETER;
    }

    private double NauticalMilesToStatuteMile(double distance) {
        return distanceToNauticalMiles(distance) * NAUTICAL_MILE_TO_STATUTE_MILE;
    }

    private double distanceToNauticalMiles(double distance) {
        return distance * 60;
    }

    private double degreeToRadian(double degree) {
        return (degree * Math.PI / 180.0);
    }

    private double radianToDegree(double radian) {
        return (radian * 180.0 / Math.PI);
    }

    public List<User> getMatchingDonors(List<User> users, double hospitalLatitude, double hospitalLongitude) {
        for (User user : users) {
            if (!isNearDonor(user.getLatitude(), user.getLongitude(), hospitalLatitude, hospitalLongitude, Double.parseDouble(user.getDistance()))) {
                users.remove(user);
            }
        }
        return users;
    }

    public boolean isNearDonor(double userLatitude, double userLongitude, double hospitalLatitude, double hospitalLongitude, double distance) {
        if (distance < this.getDistance(userLatitude, userLongitude, hospitalLatitude, hospitalLongitude)) {
            return false;
        } else {
            return true;
        }
    }

    public List<User> findMatchingDonors(BloodRequest bloodRequest) throws IllegalDataException {
        List<User> donors = usersRepository.listByBloodGroupAndAfter90DaysFromLastDonateDate(bloodRequest.getBloodGroup());
        System.out.println("Donors size : " + donors.size());

        validateMatchingDonors(bloodRequest, donors);
        List<User> filteredDonors = filterWithState(bloodRequest, donors);
        return filteredDonors;
    }

    private List<User> filterWithState(BloodRequest bloodRequest, List<User> donors) {
        String stateOfHospital = bloodRequest.getState();
        List<User> filteredList = Lists.newArrayList();
        for (User donor : donors) {
            if(stateOfHospital.equals(donor.getState()))
                filteredList.add(donor);
        }
        return filteredList;
    }

    private void validateMatchingDonors(BloodRequest bloodRequest, List<User> donors) throws IllegalDataException {
        for (User donor : donors) {
            if(isNotValidBloodGroup(bloodRequest.getBloodGroup(), donor))
                throw new IllegalDataException("The blood group should be equal to the one of request.");
            if(isNotValidDonateDate(donor))
                throw new IllegalDataException("With all found users, the last donate date of each user should be before 90 days from today.");
        }
    }

    /**
     *  Please comprehend with the following figure. :)
     * -----------------+--------
     *     NOT OK       |   OK
     * ---------|-------|--------
     *  before()|equal()| after()
     *
     */
    private boolean isNotValidDonateDate(User donor) {
        Calendar calendarCurrent = eraseTime(new Date());
        
        Calendar calendarValidDonate = eraseTime(donor.getLastDonateDate());
        calendarValidDonate.add(Calendar.DAY_OF_MONTH, 90);

        return !calendarCurrent.after(calendarValidDonate);
    }

    private boolean isNotValidBloodGroup(String bloodGroup, User donor) {
        return !(donor.getBloodGroup().equals(bloodGroup) || UNKNOWN_BLOOD_GROUP.equals(donor.getBloodGroup()));
    }

    private Calendar eraseTime(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return new GregorianCalendar(year, month, day);
    }
}