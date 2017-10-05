package stock.cryptodoc.model;

/**
 * Created by Administrator on 22-09-2017.
 */

public class GraphData {

String fsym,tsym,limit,e;

    public GraphData(String fsym, String tsym, String limit, String e) {
        this.fsym = fsym;
        this.tsym = tsym;
        this.limit = limit;
        this.e = e;
    }
}
