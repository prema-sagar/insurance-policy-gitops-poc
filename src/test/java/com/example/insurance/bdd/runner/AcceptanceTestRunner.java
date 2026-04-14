package com.example.insurance.bdd.runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

/**
 * JUnit 4 entry-point that Gradle's acceptanceTest task targets.
 *
 * Reports written to:
 *   build/cucumber-reports/index.html   — human-readable
 *   build/cucumber-reports/cucumber.json — CI dashboard / badge
 */
@RunWith(Cucumber.class)
@CucumberOptions(
        features  = "src/test/resources/features",
        glue      = "com.example.insurance.bdd.steps",
        plugin    = {
                "pretty",
                "html:build/cucumber-reports/index.html",
                "json:build/cucumber-reports/cucumber.json"
        },
        monochrome = true
)
public class AcceptanceTestRunner {
    // Body intentionally empty — Cucumber drives execution via annotations.
}
