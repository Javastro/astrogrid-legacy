package org.astrogrid.registry.dataTransformationServices.services;

public class Precession
{
  public Precession()
  {}

  public double precessRA(double oldRA, double oldDec, double oldEpoch, double newEpoch)
  {
    double newRA = oldRA + (15.0 / 3600.0) * (newEpoch - oldEpoch) * (3.074 + 1.336 * Math.sin(0.017433 * oldRA) * (Math.sin(0.0174533 * oldDec) / Math.cos(0.0174533 * oldDec)));
    return newRA;
  }

  public double precessDec(double oldRA, double oldDec, double oldEpoch, double newEpoch)
  {
    double newDec = oldDec + (1.0 / 3600.0) * (newEpoch - oldEpoch) * (20.038 * Math.cos(0.0174533 * oldRA));
    return newDec;
  }
}