package deepak.StandardChartered.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import deepak.StandardChartered.pojo.Transaction;

@RestController
public class TestController {
	@RequestMapping(value="/", method=RequestMethod.GET,produces="application/json")
	public String home() throws Exception {
		Map<Transaction, List<String>> file2Map = readFile2();
		List<String>[] response = new List[4];
		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(new ClassPathResource("./resource/file1.txt").getInputStream(), "UTF-8"))) {
			String line;
			String[] split;
			initializeResponse(response);
			while ((line = br.readLine()) != null) {
				split = line.split(";");
				Transaction t = new Transaction(split[1].trim(), split[2].trim(), split[3].trim());
				if (!removeIfMapContains(file2Map, split, response, t, 0)) {
					Transaction[] weakMatch = getPossibleCombinations(t);
					boolean isWeakMatch = isWeakMatch(file2Map, response, split, weakMatch);
					if (!isWeakMatch) {
						_XBreaks(response, split);
					}
				}
			}
			_YBreaks(file2Map, response);
		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception("Unable to read file 1");
		}
		Map<String,List<String>> sb = prepareResponse(response);
		return sb.toString();
	}

	private Map<String, List<String>> prepareResponse(List<String>[] response) {
		for (List<String> s : response) {
			System.out.println(s);
		}
		Map<String,List<String>> map=new LinkedHashMap<>();
		map.put("XY Exact Match", response[0]);
		map.put("XY weak matches", response[1]);
		map.put("X breaks", response[2]);
		map.put("Y breaks", response[3]);
		
		StringBuilder sb = new StringBuilder();
		sb.append(response[0]).append("\n").append(response[1]).append("\n").append(response[2]).append("\n")
				.append(response[3]);
		return map;
	}

	private void _XBreaks(List<String>[] response, String[] split) {
		response[2].add(split[0].trim());
	}

	private void _YBreaks(Map<Transaction, List<String>> file2Map, List<String>[] response) {
		for (Entry<Transaction, List<String>> set : file2Map.entrySet()) {
			for(String ls:set.getValue()) {
				response[3].add(ls);
			}
		}
	}

	private boolean isWeakMatch(Map<Transaction, List<String>> file2Map, List<String>[] response, String[] split,
			Transaction[] weakMatch) {
		boolean isWeakMatch = false;
		for (Transaction wm : weakMatch) {
			if (removeIfMapContains(file2Map, split, response, wm, 1)) {
				isWeakMatch = true;
				break;
			}
		}
		return isWeakMatch;
	}

	private void initializeResponse(List<String>[] response) {
		for (int i = 0; i < response.length; i++) {
			response[i] = new ArrayList<>();
		}
	}

	private boolean removeIfMapContains(Map<Transaction, List<String>> file2Map, String[] split,
			List<String>[] response, Transaction t, int typeOfMatch) {
		if (file2Map.containsKey(t)) {
			List<String> file2TransactionIds = file2Map.get(t);
			response[typeOfMatch].add(split[0].trim() + file2TransactionIds.get(file2TransactionIds.size() - 1));
			if (file2TransactionIds.size() == 1)
				file2Map.remove(t);
			return true;
		}
		return false;
	}

	private Transaction[] getPossibleCombinations(Transaction t) throws CloneNotSupportedException {
		Transaction[] weakMatch = new Transaction[8];
		double lessAmount = t.getAmount() + 0.01;
		double moreAmount = t.getAmount() - 0.01;
		Date previousDate = getPreviousDate(t.getPostingDate());
		Date nextDate = getNextDate(t.getPostingDate());

		weakMatch[0] = (Transaction) t.clone();weakMatch[0].setAmount(lessAmount);
		weakMatch[1] = (Transaction) t.clone();weakMatch[1].setAmount(moreAmount);
		weakMatch[2] = (Transaction) t.clone();weakMatch[2].setPostingDate(previousDate);
		weakMatch[3] = (Transaction) t.clone();weakMatch[3].setPostingDate(previousDate);weakMatch[3].setAmount(lessAmount);
		weakMatch[4] = (Transaction) t.clone();weakMatch[4].setPostingDate(previousDate);weakMatch[4].setAmount(moreAmount);
		weakMatch[5] = (Transaction) t.clone();weakMatch[5].setPostingDate(nextDate);
		weakMatch[6] = (Transaction) t.clone();weakMatch[6].setPostingDate(nextDate);weakMatch[6].setAmount(lessAmount);
		weakMatch[7] = (Transaction) t.clone();weakMatch[7].setPostingDate(nextDate);weakMatch[7].setAmount(moreAmount);

		return weakMatch;
	}

	private Date getNextDate(Date date) {
		Calendar next = Calendar.getInstance();
		next.setTime(date);

		if (next.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY)
			next.add(Calendar.DAY_OF_MONTH, 3);
		else if (next.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
			next.add(Calendar.DAY_OF_MONTH, 2);
		else
			next.add(Calendar.DAY_OF_MONTH, 1);
		return next.getTime();
	}

	private Date getPreviousDate(Date date) {
		Calendar previous = Calendar.getInstance();
		previous.setTime(date);
		if (previous.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY)
			previous.add(Calendar.DAY_OF_MONTH, -3);
		else if (previous.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
			previous.add(Calendar.DAY_OF_MONTH, -2);
		else
			previous.add(Calendar.DAY_OF_MONTH, -1);
		return previous.getTime();
	}
	
	private static Map<Transaction, List<String>> readFile2() throws Exception {
		Map<Transaction, List<String>> map = new HashMap<>();
		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(new ClassPathResource("./resource/file2.txt").getInputStream(), "UTF-8"))) {
			String line;
			String[] split;
			while ((line = br.readLine()) != null) {
				split = line.split(";");
				Transaction t = new Transaction(split[1].trim(), split[2].trim(), split[3].trim());
				if (!map.containsKey(t))
					map.put(t, new ArrayList<>());
				map.get(t).add(split[0].trim());
			}
			System.out.println(map.toString());
		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception("Unable to read file 2");
		}
		return map;

	}

}