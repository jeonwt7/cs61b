
public class RastHelper {

    public static boolean validBounds(RasterRequestParams params) {
        if (params.ullat < MapServer.ROOT_LRLAT
                || params.ullon > MapServer.ROOT_LRLON
                || params.lrlat > MapServer.ROOT_ULLAT
                || params.lrlon < MapServer.ROOT_ULLON) {
            return false;
        } else {
            return !(params.lrlon < params.ullon || params.ullat < params.lrlat);
        }
    }

    public static int findDepth(double lonDPP) {
        int depth = 0;
        double mapDPP = MapServer.ROOT_LONDPP;
        while (lonDPP < mapDPP && depth < Rasterer.MAX_DEPTH) {
            depth++;
            mapDPP /= 2;
        }
        return depth;
    }

    public static Double[] findIndexUllon(double ullon, int depth) {
        double mapUllon = MapServer.ROOT_ULLON;
        double mapDelta = MapServer.ROOT_LON_DELTA / Math.pow(2, depth);
        double index = 0;
        while (mapUllon + mapDelta < ullon) {
            mapUllon += mapDelta;
            index++;
        }
        return new Double[] {index, mapUllon};
    }

    public static Double[] findIndexLrlon(double lrlon, int depth) {
        double mapLrlon = MapServer.ROOT_LRLON;
        double mapDelta = MapServer.ROOT_LON_DELTA / Math.pow(2, depth);
        double index = Math.pow(2, depth) - 1;
        while (mapLrlon - mapDelta > lrlon) {
            mapLrlon -= mapDelta;
            index--;
        }
        return new Double[] {index, mapLrlon};
    }

    public static Double[] findIndexUllat(double ullat, int depth) {
        double mapUllat = MapServer.ROOT_ULLAT;
        double mapDelta = MapServer.ROOT_LAT_DELTA / Math.pow(2, depth);
        double index = 0;
        while (mapUllat - mapDelta > ullat) {
            mapUllat -= mapDelta;
            index++;
        }
        return new Double[] {index, mapUllat};
    }

    public static Double[] findIndexLrlat(double lrlat, int depth) {
        double mapLrlat = MapServer.ROOT_LRLAT;
        double mapDelta = MapServer.ROOT_LAT_DELTA / Math.pow(2, depth);
        double index = Math.pow(2, depth) - 1;
        while (mapLrlat + mapDelta < lrlat) {
            mapLrlat += mapDelta;
            index--;
        }
        return new Double[] {index, mapLrlat};
    }

    public static String[][] findGrid(int xLeft, int xRight, int yUp, int yDown, int depth) {
        String[][] toReturn = new String[yDown - yUp + 1][xRight - xLeft + 1];
        int yIndex = 0;
        for (int y = yUp; y <= yDown; y++) {
            String[] row = new String[xRight - xLeft + 1];
            int xIndex = 0;
            for (int x = xLeft; x <= xRight; x++) {
                row[xIndex] = "d" + depth + "_x" + x + "_y" + y + ".png";
                xIndex++;
            }
            toReturn[yIndex] = row;
            yIndex++;
        }
        return toReturn;
    }
}
