package com.wheretobuy.adopteuncaddie.model.openfoodfacts;

public final class Units {
    private Units() {
    }

    public static final String ENERGY_KJ = "kj";
    public static final String ENERGY_KCAL = "kcal";
    public static final String UNIT_KILOGRAM = "kg";
    public static final String UNIT_GRAM = "g";
    public static final String UNIT_MILLIGRAM = "mg";
    public static final String UNIT_MICROGRAM = "µg";
    public static final String UNIT_DV = "% DV";
    public static final String UNIT_LITER = "l";
    public static final String UNIT_DECILITRE = "dl";
    public static final String UNIT_CENTILITRE = "cl";
    public static final String UNIT_MILLILITRE = "ml";

    public static class UnitUtils{
        /**
         * Converts a given quantity's unitOfValue to grams.
         *
         * @param value The value to be converted
         * @param unitOfValue represents milligrams, 2 represents micrograms
         * @return return the converted value
         */
        public static double convertToGrams(double value, String unitOfValue) {
            if (Units.UNIT_MILLIGRAM.equalsIgnoreCase(unitOfValue)) {
                return value / 1000;
            }
            if (Units.UNIT_MICROGRAM.equalsIgnoreCase(unitOfValue)) {
                return value / 1000000;
            }
            if (Units.UNIT_KILOGRAM.equalsIgnoreCase(unitOfValue)) {
                return value * 1000;
            }
            if (Units.UNIT_LITER.equalsIgnoreCase(unitOfValue)) {
                return value * 1000;
            }
            if (Units.UNIT_DECILITRE.equalsIgnoreCase(unitOfValue)) {
                return value * 100;
            }
            if (Units.UNIT_CENTILITRE.equalsIgnoreCase(unitOfValue)) {
                return value * 10;
            }
            if (Units.UNIT_MILLILITRE.equalsIgnoreCase(unitOfValue)) {
                return value;
            }
            return value;
        }

        public static float convertToGrams(float a, String unit) {
            return (float) convertToGrams((double) a, unit);
        }

        public static float convertFromGram(float valueInGramOrMl, String targetUnit) {
            return (float) convertFromGram((double) valueInGramOrMl, targetUnit);
        }

        public static double convertFromGram(double valueInGramOrMl, String targetUnit) {
            switch (targetUnit) {
                case Units.UNIT_KILOGRAM:
                case Units.UNIT_LITER:
                    return valueInGramOrMl / 1000;
                case Units.UNIT_MILLIGRAM:
                    return valueInGramOrMl * 1000;
                case Units.UNIT_MICROGRAM:
                    return valueInGramOrMl * 1000000;
                case Units.UNIT_DECILITRE:
                    return valueInGramOrMl / 100;
                case Units.UNIT_CENTILITRE:
                    return valueInGramOrMl / 10;
            }
            return valueInGramOrMl;
        }


    }
}
