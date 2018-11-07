package com.softactive.editor.re.view;

import java.util.ArrayList;
import java.util.List;

import com.softactive.core.object.Region;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReutersRegion extends Region {
	private static final long serialVersionUID = 4063971982905715384L;
	private Integer year = 5;
	private String preCode = "";
	private String postCode = "";
	private String rSource = "";

	public ReutersRegion(Region r) {
		setId(r.getId());
	}

	public static List<ReutersRegion> getReutersRegionList(List<Region> list) {
		List<ReutersRegion> answer = new ArrayList<>();
		for (Region r : list) {
			answer.add(new ReutersRegion(r));
		}
		return answer;
	}

	public static List<Region> getRegionList(List<ReutersRegion> list) {
		List<Region> answer = new ArrayList<>();
		for (Region r : list) {
			answer.add(new ReutersRegion(r));
		}
		return answer;
	}

}
