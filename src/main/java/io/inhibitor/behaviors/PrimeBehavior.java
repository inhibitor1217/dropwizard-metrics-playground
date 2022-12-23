package io.inhibitor.behaviors;

public class PrimeBehavior {

  public long findSmallestPrimeAfter(long bound) {
    for (long i = bound + 1; ; i++) {
      if (isPrime(i)) {
        return i;
      }
    }
  }

  private boolean isPrime(long number) {
    for (long i = 2; i * i <= number; i++) {
      if (number % i == 0) {
        return false;
      }
    }
    return true;
  }
}
