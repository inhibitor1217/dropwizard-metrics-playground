package io.inhibitor.behaviors;

public class PrimeBehavior {

  public int findSmallestPrimeAfter(int bound) {
    for (int i = bound + 1; ; i++) {
      if (isPrime(i)) {
        return i;
      }
    }
  }

  private boolean isPrime(int number) {
    for (int i = 2; i * i <= number; i++) {
      if (number % i == 0) {
        return false;
      }
    }
    return true;
  }
}
