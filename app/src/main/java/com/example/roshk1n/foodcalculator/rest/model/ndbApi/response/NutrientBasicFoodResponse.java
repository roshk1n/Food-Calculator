package com.example.roshk1n.foodcalculator.rest.model.ndbApi.response;

/**
 * Created by roshk1n on 8/11/2016.
 */
public class NutrientBasicFoodResponse {
    private ReportBasicNutrients report;

    public NutrientBasicFoodResponse() {}

    public ReportBasicNutrients getReport() {
        return report;
    }

    public void setReport(ReportBasicNutrients report) {
        this.report = report;
    }
}
