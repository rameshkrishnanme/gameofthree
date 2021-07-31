package com.app.gameofthree.service;

import org.springframework.stereotype.Service;

@Service
public interface IPointsService {

	Integer computeNewPoint(Integer p1, Integer p2);
	
	boolean isWinningNumber(Integer point);

	Integer gamePoint();

	String displayFormula(Integer p1, Integer p2);
	
}
