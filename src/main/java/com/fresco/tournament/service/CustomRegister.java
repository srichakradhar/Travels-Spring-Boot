package com.fresco.tournament.service;

import java.util.Collections;
import java.util.HashMap;

public class CustomRegister {

	HashMap<Integer, Integer> register = null;

	public CustomRegister() {
		register = new HashMap<Integer, Integer>();
	}

	public void addAttendance(int[] a) {
		for (int i = 0; i < a.length; i++) {
			if (register.containsKey(a[i]))
				register.put(a[i], register.get(a[i]) + 1);
			else
				register.put(a[i], 1);
		}
	}

	public void changeAttendance(int id, char c) {
		if (c == 'A') {
			register.put(id, register.get(id) - 1);
			System.out.println("The attendance of student with id: " + id + " has been reduced by one");
		} else if (c == 'P') {
			register.put(id, register.get(id) - 1);
			System.out.println("The attendance of student with id: " + id + " has been increased by one");
		}
	}

	public void reduceRegister(int l) {
		System.out.println("Removing students with less attendance");
		register.entrySet().removeIf(entry -> entry.getValue() < l);
	}

	public int maxAttendance() {
		return Collections.max(register.values());
	}

	public void personalAttendance(int id) {
		if (register.containsKey(id))
			System.out.println("The Attendance of the student with id:" + id + "is:" + register.get(id));
		else
			System.out.println("The student with id:" + id + "has been removed due to low attendance");
	}

}