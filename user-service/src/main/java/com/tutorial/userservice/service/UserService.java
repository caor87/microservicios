package com.tutorial.userservice.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.tutorial.userservice.entity.User;
import com.tutorial.userservice.feingClients.BikeFeignClient;
import com.tutorial.userservice.feingClients.CarFeignClient;
import com.tutorial.userservice.model.Bike;
import com.tutorial.userservice.model.Car;
import com.tutorial.userservice.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	RestTemplate restTemplate; // es la clase que ofrece Spring para el acceso desde la parte cliente a
								// Servicios REST. Conceptualmente sería el equivalente al JdbcTemplate o al
								// JmsTemplate
	@Autowired
	CarFeignClient carFeignClient;
	
	@Autowired
	BikeFeignClient bikeFeignClient;

	public List<User> getALl() {
		return userRepository.findAll();
	}

	public User getUserById(int id) {
		return userRepository.findById(id).orElse(null);
	}

	public User save(User user) {
		return userRepository.save(user);
	}

	public List<Car> getCars(int userId) {

		List<Car> cars = restTemplate.getForObject("http://localhost:8082/car/byUser/" + userId, List.class);
		return cars;
	}

	public List<Bike> getBike(int userId) {

		List<Bike> bikes = restTemplate.getForObject("http://localhost:8083/bike/byUser/" + userId, List.class);
		return bikes;
	}
	
	public Car saveCar(int userId,Car car) {
		car.setUserId(userId);
		Car carNew = carFeignClient.saveCar(car);
		return carNew;
	}
	
	public Bike saveBike(int userId, Bike bike) {
		bike.setUserId(userId);
		Bike bikeNew = bikeFeignClient.saveBike(bike);
		return bikeNew;
	}
	
	public Map<String, Object> getUserAndVehicles(int userId){
		
		Map<String, Object> result = new HashMap<>();
		User user = userRepository.findById(userId).orElse(null);
		if(user == null) {
			result.put("Mensaje", "No eiste el usuario");
			return result;
		}
		result.put("User", user);
		List<Car> cars = carFeignClient.getCars(userId);
		if(cars.isEmpty())
			result.put("Cars", "El usuaio no tiene automovil");
		else 
			result.put("Cars", cars);
		
		List<Bike> bikes = bikeFeignClient.getBikes(userId);
		if(bikes.isEmpty())
			result.put("Bikes", "El usuario no tiene motocicleta");
		else
			result.put("Bikes", bikes);
		
		return result;	
	}
	

}
