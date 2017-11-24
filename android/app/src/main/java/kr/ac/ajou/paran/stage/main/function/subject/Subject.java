package kr.ac.ajou.paran.stage.main.function.subject;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import kr.ac.ajou.paran.R;
import kr.ac.ajou.paran.util.Callback;
import kr.ac.ajou.paran.util.DB;
import kr.ac.ajou.paran.util.NetworkAsync;

import static android.icu.text.Normalizer.YES;
import static kr.ac.ajou.paran.R.id.rb1;
import static kr.ac.ajou.paran.R.id.rb2;
import static kr.ac.ajou.paran.R.id.rgbtn;
import static kr.ac.ajou.paran.R.id.subjectlv;


/**
 * Created by user on 2017-08-11.
 */

/*public class Subject extends FunctionType*/

public class Subject extends AppCompatActivity implements Callback {

    ListView subjectlv;
    public static LinkedHashMap<String, String> basicMap;
    private static LinkedHashMap<String, String> typeMap;
    private static LinkedHashMap<String, String> majorMap;
    private Callback mCallback;

    public boolean checkBackPress;
    private final String year = "2017";
    private final String semester = "U0002003";
    private String type;
    private String major;
    Button btnback;

    final List<String> list = new ArrayList<>();
    final List<String> list2 = new ArrayList<>();
    final List<String> list3 = new ArrayList<>();
    final List<String> list4 = new ArrayList<>();

    DB db;
    /*public Subject(){
        super("수강과목 보기",R.layout.activity_subject);
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        subjectlv = (ListView) findViewById(R.id.subjectlv);
        btnback = (Button)findViewById(R.id.btnback);
        mCallback = this;
        db = new DB(getApplicationContext(), "mydb2.db", null, 1);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);

        subjectlv.setAdapter(arrayAdapter);
        majorMap = setHashMap(majorMap);
        typeMap = setHashMap3(typeMap);
        basicMap = setHashMap1(basicMap);

        list.add("전공과목");
        list.add("교양과목");
        list.add("기초과목");
        list.add("영역별교양");


        subjectlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                Set<String> set = typeMap.keySet();
                Iterator<String> itr = set.iterator();


                while(itr.hasNext())
                {

                    String str = itr.next();
                    String str2 = (String)subjectlv.getItemAtPosition(i);


                    if(str2 == "전공과목")
                    {

                        type = typeMap.get(str2);

                        Set<String> setM = majorMap.keySet();
                        Iterator<String> itrM = setM.iterator();

                        while(itrM.hasNext())
                        {
                            String majorstring = itrM.next();
                            list2.add(majorstring);
                        }


                        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, list2);
                        arrayAdapter2.notifyDataSetChanged();
                        subjectlv.setAdapter(arrayAdapter2);

                        subjectlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {



                                Set<String> set = majorMap.keySet();
                                Iterator<String> itr = set.iterator();


                                while(itr.hasNext())
                                {
                                    String str = itr.next();
                                    String str2 = (String)subjectlv.getItemAtPosition(i);

                                    if(str == str2)
                                    {
                                        major = majorMap.get(str2);
                                        NetworkAsync networkAsync = new NetworkAsync(semester, type, major, mCallback);
                                        networkAsync.execute();
                                        break;

                                    }

                                }


                                subjectlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                                        /*String str = (String)subjectlv.getItemAtPosition(i);
                                        Intent intent = new Intent(Subject.this, SubjectManage.class);
                                        intent.putExtra("eom", str);
                                        startActivity(intent);*/

                                        AlertDialog.Builder ad = new AlertDialog.Builder(Subject.this);
                                        ad.setTitle("Title");
                                        ad.setMessage("재수강 과목이시면 체크 후 YES를, 아니면 NO를 눌러주세요.");

                                       // LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                      //  final ViewGroup view2 = (ViewGroup)inflater.inflate(R.layout.activity_subject2, null);
                                      //  ad.setView(view2);


                                        /*final RadioButton rb1 = (RadioButton)view2.findViewById(R.id.rb1);
                                        final RadioButton rb2 = (RadioButton)view2.findViewById(R.id.rb2);
                                        final RadioGroup rgbtn = (RadioGroup)view2.findViewById(R.id.rgbtn);*/

                                        final RadioGroup rgbtn = new RadioGroup(Subject.this);
                                        final RadioButton rbbtn = new RadioButton(Subject.this);

                                        rbbtn.setText("O(재수강은 선택후 YES)");

                                        ad.setView(rgbtn);
                                        ad.setView(rbbtn);

                                        String sql = "select _id from subjectinfo2";
                                        Cursor cursor = db.getReadableDatabase().rawQuery(sql, null);
                                        final int c = cursor.getCount();

                                        //dbHelper.insertsubject(c, retake, mandate, name);

                                        ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                String str = rbbtn.getText().toString();
                                                String retake = str.substring(0,1);
                                                //String str3 = (String)subjectlv.getItemAtPosition(i);
                                                String tmp = null;
                                                String name = null;
                                                String type = null;
                                                try{


                                                   tmp = ((String)subjectlv.getItemAtPosition(i));
                                                   name = tmp.substring(tmp.indexOf(":")+1, tmp.indexOf("구분"));
                                                   type = tmp.substring(tmp.indexOf("구분")+4, tmp.length());
                                                   Toast.makeText(getApplicationContext(), c+"", Toast.LENGTH_SHORT).show();
                                                   db.insertSubject(c+1, retake, type, name);
                                                   Intent intent = new Intent(Subject.this, SubjectManage.class);
                                                   startActivity(intent);

                                               }

                                               catch (Exception e)
                                               {
                                                   e.printStackTrace();
                                               }

                                                dialog.dismiss();

                                            }
                                        });

                                        ad.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {


                                                //String str3 = (String)subjectlv.getItemAtPosition(i);
                                                String tmp = null;
                                                String name = null;
                                                String type = null;
                                                try{


                                                    tmp = ((String)subjectlv.getItemAtPosition(i));
                                                    name = tmp.substring(tmp.indexOf(":")+1, tmp.indexOf("구분"));
                                                    type = tmp.substring(tmp.indexOf("구분")+4, tmp.length());
                                                    Toast.makeText(getApplicationContext(), c+"", Toast.LENGTH_SHORT).show();
                                                    db.insertSubject(c+1, "X", type, name);
                                                    Intent intent = new Intent(Subject.this, SubjectManage.class);
                                                    startActivity(intent);

                                                }

                                                catch (Exception e)
                                                {
                                                    e.printStackTrace();
                                                }


                                                dialog.dismiss();
                                            }
                                        });

                                        ad.create();
                                        ad.show();

                                    }
                                });



                            }
                        });



                    }

                    else if (str2 == "교양과목")
                    {

                        type = typeMap.get(str2);

                        subjectlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                                NetworkAsync networkAsync = new NetworkAsync(semester, type, mCallback);
                                networkAsync.execute();




                            }
                        });




                    }

                    else if (str2 == "기초과목")
                    {

                        type = typeMap.get(str2);

                        Set<String> setM = basicMap.keySet();
                        Iterator<String> itrM = setM.iterator();


                        while(itrM.hasNext())
                        {
                            String basicstring = itrM.next();
                            list4.add(basicstring);
                        }


                        ArrayAdapter<String> arrayAdapter4 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, list4);
                        arrayAdapter4.notifyDataSetChanged();
                        subjectlv.setAdapter(arrayAdapter4);

                        subjectlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {



                                Set<String> set = basicMap.keySet();
                                Iterator<String> itr = set.iterator();


                                while(itr.hasNext())
                                {
                                    String str = itr.next();
                                    String str2 = (String)subjectlv.getItemAtPosition(i);

                                    if(str == str2)
                                    {
                                        major = basicMap.get(str2);
                                        NetworkAsync networkAsync = new NetworkAsync(semester, type, major, mCallback);
                                        networkAsync.execute();
                                        break;

                                    }

                                }


                            }
                        });

                    }

                }



            }
        });


    }

    public static LinkedHashMap<String, String> setHashMap3(LinkedHashMap<String, String> typeMap)
    {
        typeMap = new LinkedHashMap<>();

        typeMap.put("전공과목", "U0209001");
        typeMap.put("교양과목", "U0209002");
        typeMap.put("기초과목", "U0209003");
        //typeMap.put("공학기초", "U0209004");
        typeMap.put("영역별교양","U0209005");
        //typeMap.put("학점교류", "U0209006");
        //typeMap.put("공학인증교양", "U0209007");

        return typeMap;
    }


    public static LinkedHashMap<String, String> setHashMap(LinkedHashMap<String, String> majorMap)
    {
        majorMap = new LinkedHashMap<>();
        majorMap.put("기계공학전공", "DS03001002001");
        majorMap.put("기계공학전공(과)", "DS03001021001");
        majorMap.put("산업정보시스템공학전공", "DS03001003001");
        majorMap.put("산업공학전공(과)", "DS03001022001");
        majorMap.put("산업정보시스템공학전공(과)", "DS03001022002");
        majorMap.put("화학공학전공", "DS03001004002");
        majorMap.put("화학공학전공(과)", "DS03001023001");
        majorMap.put("신소재공학전공", "DS03001004001");
        majorMap.put("신소재공학전공(과)", "DS03001024001");
        majorMap.put("응용화학전공", "DS03001005001");
        majorMap.put("생명공학전공", "DS03001005002");
        majorMap.put("응용화학생명공학전공", "DS03001006001");
        majorMap.put("응용화학생명공학전공(과)", "DS03001025001");
        majorMap.put("환경공학전공", "DS03001007001");
        majorMap.put("환경안전공학전공(과)", "DS03001026001");
        majorMap.put("건설시스템공학전공", "DS03001007002");
        majorMap.put("건설시스템공학전공(과)", "DS03001027001");
        majorMap.put("교통시스템공학전공", "DS03001007003");
        majorMap.put("교통시스템공학전공(과)", "DS03001028001");
        majorMap.put("건축학전공", "DS03001008001");
        majorMap.put("건축학전공(과)", "DS03001029002");
        majorMap.put("건축학전공(5년)", "DS03001008002");
        majorMap.put("건축학전공(5년)(과)", "DS03001029003");
        majorMap.put("건축공학전공", "DS03001008003");
        majorMap.put("건축공학전공(과)", "DS03001029001");
        majorMap.put("전자공학전공", "DS03002002001");
        majorMap.put("전자공학전공(과)", "DS03002021001");
        majorMap.put("소프트웨어및컴퓨터공학전공(과)", "DS030020201");
        majorMap.put("사이버보안전공(과)", "DS030020101");
        majorMap.put("정보컴퓨터공학전공", "DS03002003001");
        majorMap.put("컴퓨터공학전공(과)", "DS03002022001");
        majorMap.put("소프트웨어보안전공(과)", "DS03002022002");
        majorMap.put("정보컴퓨터공학전공(과)", "DS03002022003");
        majorMap.put("소프트웨어융합전공", "DS03002003002");
        majorMap.put("미디어학전공", "DS03002004001");
        majorMap.put("미디어콘텐츠전공(과)", "DS03002024001");
        majorMap.put("미디어학전공(과)", "DS03002024002");
        majorMap.put("소셜미디어전공(과)", "DS03002024003");
        majorMap.put("공군ICT전공(과)", "DS03002025001");
        majorMap.put("ICT융합전공(과)", "DS030020202");
        majorMap.put("수학전공", "DS03003002001");
        majorMap.put("수학전공(과)", "DS03003021001");
        majorMap.put("물리학전공", "DS03003002002");
        majorMap.put("물리학전공(과)", "DS03003022001");
        majorMap.put("화학전공", "DS03003002003");
        majorMap.put("화학전공(과)", "DS03003023001");
        majorMap.put("생명과학전공", "DS03003002004");
        majorMap.put("생명과학전공(과)", "DS03003024001");
        majorMap.put("경영학전공", "DS03004002001");
        majorMap.put("경영학전공(과)", "DS03004021001");
        majorMap.put("e-비즈니스학전공", "DS03004003001");
        majorMap.put("e-비즈니스학전공(과)", "DS03004022001");
        majorMap.put("금융공학전공", "DS0300400501");
        majorMap.put("금융공학전공(과)", "DS03004023001");
        majorMap.put("스포츠마케팅학전공", "DS03004004001");
        majorMap.put("스포츠마케팅학전공(과)", "DS03004021002");
        majorMap.put("국어국문학전공", "DS03005002001");
        majorMap.put("국어국문학전공(과)", "DS03005021001");
        majorMap.put("영어영문학전공", "DS03005002002");
        majorMap.put("영어영문학전공(과)", "DS03005022001");
        majorMap.put("불어불문학전공", "DS03005002003");
        majorMap.put("불어불문학전공(과)", "DS03005023001");
        majorMap.put("사학전공", "DS03005002004");
        majorMap.put("사학전공(과)", "DS03005024001");
        majorMap.put("문화콘텐츠학전공", "DS03005002005");
        majorMap.put("문화콘텐츠학전공(과)", "DS03005025001");
        majorMap.put("경제학전공", "DS03006002001");
        majorMap.put("경제학전공(과)", "DS03006021001");
        majorMap.put("행정학전공", "DS03006002002");
        majorMap.put("행정학전공(과)", "DS03006022001");
        majorMap.put("심리학전공", "DS03006002003");
        majorMap.put("심리학전공(과)", "DS03006023001");
        majorMap.put("사회학전공", "DS03006002004");
        majorMap.put("사회학전공(과)", "DS03006024001");
        majorMap.put("정치외교학전공", "DS03006002005");
        majorMap.put("정치외교학전공(과)", "DS03006025001");
        majorMap.put("스포츠레저학전공", "DS03006003001");
        majorMap.put("스포츠레저학전공(과)", "DS03006026001");
        majorMap.put("법학전공", "DS03007002001");
        majorMap.put("법학전공(과)", "DS03007021001");
        majorMap.put("의학전공(과)", "DS03008002001");
        majorMap.put("간호학전공(과)", "DS03009001001");
        majorMap.put("간호학전공(특별과정)(과)", "DS03009002001");
        majorMap.put("자유전공", "DS03010002001");
        majorMap.put("약학전공(과)", "DS03013021001");
        majorMap.put("약학전공", "DS03013002001");
        majorMap.put("한국학전공(과)", "DS03012001006");
        majorMap.put("기초의과학전공", "DS03003002801");
        majorMap.put("국제통상전공(과)", "DS03012001001");
        majorMap.put("문화산업과커뮤니케이션전공", "DS03006002801");
        majorMap.put("문화산업과커뮤니케이션전공(과)", "DS03006027001");
        majorMap.put("중국지역연구전공(과)", "DS03012001002");
        majorMap.put("문화학전공", "DS03005002801");
        majorMap.put("일본지역연구전공(과)", "DS03012001003");
        majorMap.put("지역연구전공(유럽)(과)", "DS03012001004");
        majorMap.put("지역연구전공(미국)(과)", "DS03012001005");
        majorMap.put("인문사회데이터분석전공(과)", "DS030020301");
        majorMap.put("응용화학전공(과)", "DS03001025002");
        majorMap.put("생명공학전공(과)", "DS03001025003");
        majorMap.put("소프트웨어융합전공(과)", "DS03002023001");
        majorMap.put("기초의과학전공(과)", "DS03003025001");
        majorMap.put("융합시스템공학전공(과)", "DS03001030001");
        majorMap.put("ICT융합전공(과)(폐지)", "DS03002022004");
        majorMap.put("자동차SW전공(과)", "DS030020401");
        majorMap.put("디지털휴머니티전공(과)", "DS0300502601");


        return majorMap;
    }


    public static LinkedHashMap<String, String> setHashMap1(LinkedHashMap<String, String> basicMap) {
        basicMap = new LinkedHashMap<>();

        basicMap.put("공통", "00");
        basicMap.put("기계공학부", "DS03001002");
        basicMap.put("기계공학과", "DS03001021");
        basicMap.put("산업정보시스템공학부", "DS03001003");
        basicMap.put("산업공학과", "DS03001022");
        basicMap.put("화공ㆍ신소재공학부", "DS03001004");
        basicMap.put("화학공학과", "DS03001023");
        basicMap.put("신소재공학과", "DS03001024");
        basicMap.put("생명ㆍ분자공학부", "DS03001005");
        basicMap.put("응용화학생명공학부", "DS03001006");
        basicMap.put("응용화학생명공학과", "DS03001025");
        basicMap.put("환경안전공학과", "DS03001026");
        basicMap.put("건설시스템공학과", "DS03001027");
        basicMap.put("교통시스템공학과", "DS03001028");
        basicMap.put("건축학부", "DS03001008");
        basicMap.put("건축학과", "DS03001029");
        basicMap.put("전자공학부", "DS03002002");
        basicMap.put("전자공학과", "DS03002021");
        basicMap.put("소프트웨어학과", "DS0300202");
        basicMap.put("정보컴퓨터공학부", "DS03002003"); //14일부터 한거
        basicMap.put("미디어학부", "DS03002004");
        basicMap.put("미디어학과", "DS03002024");
        basicMap.put("국방디지털융합학과", "DS03002025");
        basicMap.put("사이버보안학과", "DS0300201");
        basicMap.put("수학과", "DS03003021");
        basicMap.put("물리학과", "DS03003022");
        basicMap.put("화학과", "DS03003023");
        basicMap.put("생명과학과", "DS03003024");
        basicMap.put("경영학부", "DS03004002");
        basicMap.put("경영학과", "DS03004021");
        basicMap.put("e-비즈니스학부", "DS03004003");
        basicMap.put("e-비즈니스학과", "DS03004022");
        basicMap.put("금융공학부", "DS03004005");
        basicMap.put("금융공학과", "DS03004023");
        basicMap.put("스포츠마케팅학부", "DS03004004");
        basicMap.put("글로벌경영학과", "DS03004024");
        basicMap.put("인문학부", "DS03005002");
        basicMap.put("국어국문학과", "DS03005021");
        basicMap.put("사회과학대학(과)", "DS03006029");
        basicMap.put("영어영문학과", "DS03005022");
        basicMap.put("불어불문학과", "DS03005023");
        basicMap.put("사학과", "DS03005024");
        basicMap.put("문화콘텐츠학과", "DS03005025");
        basicMap.put("사회과학부", "DS03006002");
        basicMap.put("경제학과", "DS03006021");
        basicMap.put("행정학과", "DS03006022");
        basicMap.put("심리학과", "DS03006023");
        basicMap.put("사회학과", "DS03006024");
        basicMap.put("정치외교학과", "DS03006025");
        basicMap.put("스포츠레저학과", "DS03006026");
        basicMap.put("스포츠레저학부", "DS03006003");
        basicMap.put("법학부", "DS03007002");
        basicMap.put("법학과", "DS03007021");
        basicMap.put("의학과", "DS03008002");
        basicMap.put("간호학과", "DS03009001");
        basicMap.put("간호학과(특별과정)", "DS03009002");
        basicMap.put("자유전공", "DS03010002");
        basicMap.put("약학과", "DS03013021");
        basicMap.put("약학부", "DS03013002");
        basicMap.put("국제학부", "DS03012001");
        basicMap.put("문화산업과 커뮤니케이션전공", "DS03006027");
        basicMap.put("인문사회데이터분석전공", "DS0300203");
        basicMap.put("정보컴퓨터공학과", "DS03002022");
        basicMap.put("소프트웨어융합학과", "DS03002023");
        basicMap.put("기초의과학전공", "DS03003025");
        basicMap.put("융합시스템공학과", "DS03001030");
        basicMap.put("자동차SW전공", "DS0300204");
        basicMap.put("디지털휴머니티전공", "DS03005026");


        return basicMap;
    }

    @Override
    public void getReturn(Object o) {


        String string = o.toString();
        StringTokenizer s = new StringTokenizer(string, "E");

        while (s.hasMoreTokens()) {

            String name = s.nextToken();
            String mandate = s.nextToken();


            list3.add("과목 :" + name + "  " + "구분 :"+ mandate );
        }


        ArrayAdapter<String> arrayAdapter3 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, list3);
        arrayAdapter3.notifyDataSetChanged();
        subjectlv.setAdapter(arrayAdapter3);




       /* String string = o.toString();
        StringTokenizer s = new StringTokenizer(string);
        ArrayList arrayList = new ArrayList();

        while (s.hasMoreTokens()) {

            arrayList.add(s.nextToken());
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList);
        subjectlv.setAdapter(arrayAdapter);
*/


    }


}