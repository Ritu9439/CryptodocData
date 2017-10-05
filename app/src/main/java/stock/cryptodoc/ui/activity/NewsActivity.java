package stock.cryptodoc.ui.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import stock.cryptodoc.R;
import stock.cryptodoc.adapter.NewsAdapter;
import stock.cryptodoc.model.NewsData;


public class NewsActivity extends AppCompatActivity {

String url="http://www.bitnewz.net/rss/Feed/25";
    RecyclerView newslist;
    ArrayList<NewsData> arrayList=new ArrayList<>();
    NewsAdapter newsAdapter;
    RecyclerView.LayoutManager layoutManager;
    ProgressDialog progressdialog;
    String imgRegex = "<[iI][mM][gG][^>]+[sS][rR][cC]\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        progressdialog=new ProgressDialog(NewsActivity.this);
        newslist= (RecyclerView) findViewById(R.id.newslist);
        layoutManager=new LinearLayoutManager(NewsActivity.this);
        newslist.setLayoutManager(layoutManager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setNavigationIcon(R.mipmap.ic_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });
        new MyNewsAsync().execute();
    }

    private class MyNewsAsync extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressdialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            DocumentBuilderFactory documentBuilderFactory=DocumentBuilderFactory.newInstance();
            try {
                DocumentBuilder documentBuilder=documentBuilderFactory.newDocumentBuilder();
                Document document=documentBuilder.parse(url);
                document.normalize();
                NodeList nodeList=document.getElementsByTagName("item");
                for (int i=0;i<nodeList.getLength();i++)
                {
                    Node node=nodeList.item(i);
                    Element element= (Element) node;

                    String title=getDOMdata(element,"title");
                    String pubDate=getDOMdata(element,"pubDate");
                    String description=getDOMdata(element,"description");
                    Pattern p = Pattern.compile(imgRegex);
                    Matcher m = p.matcher(description);
                    String link=getDOMdata(element,"link");
                    NewsData newsdata=new NewsData("",title,pubDate,"",description,link);
                    if (m.find()) {
                        String imgSrc = m.group(1);
                        newsdata.setImage(imgSrc);

                    }

                    arrayList.add(newsdata);
                }



            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        private String getDOMdata(Element element, String title) {
            NodeList nolist=element.getElementsByTagName(title);
            Node child=nolist.item(0);
            Element ele= (Element) child;
            String data=ele.getTextContent();
            return data;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (progressdialog.isShowing()){
                progressdialog.dismiss();
            }

            newsAdapter=new NewsAdapter(arrayList,NewsActivity.this);
            newslist.setAdapter(newsAdapter);
        }
    }
}
