package com.driver.services;

import com.driver.EntryDto.AddTrainEntryDto;
import com.driver.EntryDto.SeatAvailabilityEntryDto;
import com.driver.model.Passenger;
import com.driver.model.Station;
import com.driver.model.Ticket;
import com.driver.model.Train;
import com.driver.repository.TrainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TrainService {

    @Autowired
    TrainRepository trainRepository;

    public Integer addTrain(AddTrainEntryDto trainEntryDto){

        //Add the train to the trainRepository
        //and route String logic to be taken from the Problem statement.
        //Save the train and return the trainId that is generated from the database.
        //Avoid using the lombok library
        Train train =new Train();
        train.setDepartureTime(trainEntryDto.getDepartureTime());
        train.setNoOfSeats(trainEntryDto.getNoOfSeats());
        String route="";
        int size=trainEntryDto.getStationRoute().size();
        int count=0;
        for(Station station:trainEntryDto.getStationRoute()){
            route+=station;
            count++;
            if(count!=size) route+=",";
        }
        train.setRoute(route);
       Train savedTrain= trainRepository.save(train);
        return savedTrain.getTrainId();
    }

    //doubt in question
    public Integer calculateAvailableSeats(SeatAvailabilityEntryDto seatAvailabilityEntryDto){

        //Calculate the total seats available
        //Suppose the route is A B C D
        //And there are 2 seats avaialble in total in the train
        //and 2 tickets are booked from A to C and B to D.
        //The seat is available only between A to C and A to B.
        // If a seat is
        // empty between 2 station it will be counted to our final ans
        //even if that seat is booked post the
        // destStation or before the boardingStation
        //Inshort : a train has totalNo of seats and there
        // are tickets from and to different locations
        //We need to find out the available seats between the given 2 stations.

       return null;
    }

    public Integer calculatePeopleBoardingAtAStation(Integer trainId,Station station) throws Exception{

        //We need to find out the number of people who will be boarding a train from a particular station
        //if the trainId is not passing through that station
        //throw new Exception("Train is not passing from this station");
        //  in a happy case we need to find out the number of such people.
        Train train =trainRepository.findByTrainId(trainId);
        String stationArray[]=train.getRoute().split(",");
        boolean found=false;
        for(String stationElement:stationArray){
            if(stationElement.equals(station.toString())){
                found=true;
                break;
            }
        }
        if(found==false) throw new Exception("Train is not passing from this station");
        List<Ticket> ticketList=train.getBookedTickets();
        int count=0;
        for(Ticket ticket:ticketList){
            if(ticket.getFromStation().equals(station)){
                count+=ticket.getPassengersList().size();
            }
        }



        return count;
    }

    public Integer calculateOldestPersonTravelling(Integer trainId){

        //Throughout the journey of the train between any 2 stations
        //We need to find out the age of the oldest person that is travelling the train
        //If there are no people travelling in that train you can return 0
        Integer maxAge=0;
        Train train=trainRepository.findByTrainId(trainId);
        for(Ticket ticket:train.getBookedTickets()){
            for(Passenger passenger: ticket.getPassengersList()){
                if(passenger.getAge()>maxAge)
                    maxAge=passenger.getAge();
            }
        }


        return maxAge;
    }

    public List<Integer> trainsBetweenAGivenTime(Station station, LocalTime startTime, LocalTime endTime){

        //When you are at a particular station you need to find out the number of trains that will pass through a given station
        //between a particular time frame both start time and end time included.
        //You can assume that the date change doesn't need to be done ie the travel will certainly happen with the same date (More details
        //in problem statement)
        //You can also assume the seconds and milli seconds value will be 0 in a LocalTime format.
        List<Integer> trainIds=new ArrayList<>();
        List<Train> totalTrain=trainRepository.findAll();
        List<Train> newTrainList=new ArrayList<>();
        //finding out which train have the desired stations and adding it to new trainList
        for(Train train:totalTrain){
            String stationList[]=train.getRoute().split(",");
            for(String stationString:stationList){
                if(stationString.equals(station.toString())){
                    newTrainList.add(train);
                    break;
                }
            }
        }
        //finding out the position of the station in each station's list and checking whether the are reaching there
        for(Train train:newTrainList){
            String stationList[]=train.getRoute().split(",");
            int index=-1;
            for(int i=0;i<stationList.length;i++){
                if(stationList[i].equals(station.toString())){
                    index=i;
                    break;
                }
            }
            LocalTime toA=train.getDepartureTime().plusHours(index);//toa->time of arrival
            if(toA.equals(startTime)||toA.equals(endTime) ||(toA.isAfter(startTime) && toA.isBefore(endTime))){
                trainIds.add(train.getTrainId());
            }
        }

        return trainIds;
    }

}
