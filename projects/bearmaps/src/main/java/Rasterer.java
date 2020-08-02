/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {
    /** The max image depth level. */
    public static final int MAX_DEPTH = 7;

    /**
     * Takes a user query and finds the grid of images that best matches the query. These images
     * will be combined into one big image (rastered) by the front end. The grid of images must obey
     * the following properties, where image in the grid is referred to as a "tile".
     * <ul>
     *     <li>The tiles collected must cover the most longitudinal distance per pixel (LonDPP)
     *     possible, while still covering less than or equal to the amount of longitudinal distance
     *     per pixel in the query box for the user viewport size.</li>
     *     <li>Contains all tiles that intersect the query bounding box that fulfill the above
     *     condition.</li>
     *     <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     * </ul>
     * @param params The RasterRequestParams containing coordinates of the query box and the browser
     *               viewport width and height.
     * @return A valid RasterResultParams containing the computed results.
     */
    public RasterResultParams getMapRaster(RasterRequestParams params) {

        /* Hint: Define additional classes to make it easier to pass around multiple values, and
         * define additional methods to make it easier to test and reason about code. */

        if (!RastHelper.validBounds(params)) {
            return RasterResultParams.queryFailed();
        } else {
            RasterResultParams.Builder builder = new RasterResultParams.Builder();
            int depth = RastHelper.findDepth(lonDPP(params.lrlon, params.ullon, params.w));
            builder.setDepth(depth);
            Double[] leftLon = RastHelper.findIndexUllon(params.ullon, depth);
            Double[] rightLon = RastHelper.findIndexLrlon(params.lrlon, depth);
            Double[] upperLat = RastHelper.findIndexUllat(params.ullat, depth);
            Double[] lowerLat = RastHelper.findIndexLrlat(params.lrlat, depth);
            builder.setRasterUlLon(leftLon[1]);
            builder.setRasterLrLon(rightLon[1]);
            builder.setRasterUlLat(upperLat[1]);
            builder.setRasterLrLat(lowerLat[1]);
            builder.setRenderGrid(RastHelper.findGrid(leftLon[0].intValue(), rightLon[0].intValue(),
                    upperLat[0].intValue(), lowerLat[0].intValue(), depth));
            builder.setQuerySuccess(true);
            return builder.create();
        }
    }

    /**
     * Calculates the lonDPP of an image or query box
     * @param lrlon Lower right longitudinal value of the image or query box
     * @param ullon Upper left longitudinal value of the image or query box
     * @param width Width of the query box or image
     * @return lonDPP
     */
    private double lonDPP(double lrlon, double ullon, double width) {
        return (lrlon - ullon) / width;
    }
}
