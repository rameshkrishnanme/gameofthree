package com.app.gameofthree.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PointsServiceImpl implements IPointsService {

	@Value("${game.divideBy}")
	private int divideBy;
	
	@Value("${game.start}")
	private int START;
	
	@Value("${game.end}")
	private int END;
	

	@Override
	public String displayFormula(Integer p1, Integer p2) {
		Integer newPoint = p1 + p2;
		Integer result =  Math.abs(newPoint/divideBy);
		return String.format("(%s + %s) / %s = %s", p1, p2, divideBy, result);
	}
	
	@Override
	public Integer computeNewPoint(Integer p1, Integer p2) {
		Integer newPoint = p1 + p2;
		return Math.abs(newPoint/divideBy);
	}

	@Override
	public boolean isWinningNumber(Integer point) {
		return point <= 1 ;
	}

	@Override
	public Integer gamePoint() {
		Random random = new Random();
	    int asInt = random.ints(START, END)
	      .findFirst()
	      .getAsInt();
		return asInt;
	}

}
