package neurocars.utils;

import java.util.Comparator;

import neurocars.valueobj.RaceResult;

public class RaceResultComparator implements Comparator<RaceResult> {

  public int compare(RaceResult r1, RaceResult r2) {
    return (int) Math.signum(r1.getTotal() - r2.getTotal());
  }

}
