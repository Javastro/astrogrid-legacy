package org.astrogrid.dataTransformationServices.services;

public class WavelengthConverter {
  double c = 300000000.0;
  public WavelengthConverter() {
  }

  public double wavelengthToFrequency(double wavelength) {
    double frequency = c / wavelength;
    return frequency;
  }

  public double frequencyToWavelength(double frequency){
    double wavelength = c / frequency;
    return wavelength;
  }
}