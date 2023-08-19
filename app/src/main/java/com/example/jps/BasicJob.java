package com.example.jps;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import android.os.AsyncTask;
import android.widget.CheckBox;
import android.database.Cursor;


public class BasicJob extends AppCompatActivity {

    private RecyclerView recyclerView;
    private JobAdapter jobAdapter;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basic_job);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DatabaseHelper(BasicJob.this);

        // Start AsyncTask to read CSV file
        new ReadCSVFileTask().execute("장애인구직정보_전처리.csv");
    }

    private class ReadCSVFileTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... filenames) {
            List<String[]> allData = new ArrayList<>();
            try {
                InputStreamReader inputStreamReader = new InputStreamReader(getAssets().open(filenames[0]), "UTF-8");
                CSVReader csvReader = new CSVReader(inputStreamReader);

                // 헤더 행 스킵
                csvReader.readNext();

                String[] nextLine;
                while ((nextLine = csvReader.readNext()) != null) {
                    allData.add(nextLine);
                }
                csvReader.close();
            } catch (IOException | CsvValidationException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Cursor cursor = dbHelper.getAllJobs();
            jobAdapter = new JobAdapter(cursor);
            recyclerView.setAdapter(jobAdapter);
        }
    }

    class JobViewHolder extends RecyclerView.ViewHolder {
        TextView textView1, textView2, textView3, textView4, textView5, textView6;
        CheckBox checkBox;
        JobViewHolder(View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.textView1);
            textView2 = itemView.findViewById(R.id.textView2);
            textView3 = itemView.findViewById(R.id.textView3);
            textView4 = itemView.findViewById(R.id.textView4);
            textView5 = itemView.findViewById(R.id.textView5);
            textView6 = itemView.findViewById(R.id.textView6);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }
    public static class JobData {
        private String[] data;
        private boolean isChecked;

        // static 변수로 체크된 아이템들을 저장할 리스트 추가
        public static List<JobData> checkedItems = new ArrayList<>();

        public JobData(String[] data) {
            this.data = data;
        }

        public String[] getData() {
            return data;
        }

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            if(checked && !this.isChecked) {
                // 아이템을 체크 리스트에 추가
                checkedItems.add(this);
            } else if (!checked && this.isChecked) {
                // 아이템을 체크 리스트에서 삭제
                checkedItems.remove(this);
            }
            this.isChecked = checked;
        }

        // static 메서드로 체크된 아이템 리스트를 얻는 메서드 추가
        public static List<JobData> getCheckedItems() {
            return checkedItems;
        }
    }

    class JobAdapter extends RecyclerView.Adapter<JobViewHolder> {
        private List<JobData> jobDataList;

        JobAdapter(Cursor cursor) {
            this.jobDataList = new ArrayList<>();
            while(cursor.moveToNext()) {
                String companyName = getColumnValue(cursor, "company_name");
                String jobType = getColumnValue(cursor, "job_type");
                String company_contract = getColumnValue(cursor, "contract");
                String company_address = getColumnValue(cursor, "Address");
                String company_StartDay = getColumnValue(cursor, "Start_Day");
                String company_EndDay = getColumnValue(cursor, "End_Day");

                if(companyName != null && jobType != null && company_contract != null && company_address != null && company_StartDay != null && company_EndDay != null) {
                    String[] data = new String[] { companyName, jobType, company_contract, company_address, company_StartDay, company_EndDay };
                    this.jobDataList.add(new JobData(data));
                }
            }
        }
        private String getColumnValue(Cursor cursor, String columnName) {
            int index = cursor.getColumnIndex(columnName);
            if(index != -1) {
                return cursor.getString(index);
            }
            return null;
        }
        public JobViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
            return new JobViewHolder(view);
        }

        public void onBindViewHolder(JobViewHolder holder, int position) {
            JobData job = jobDataList.get(position);

            holder.textView1.setText("회사명 : " + job.getData()[0]);
            holder.textView2.setText("직종 업무 : " + job.getData()[1]);
            holder.textView3.setText("계약 구분 : " + job.getData()[2]);
            holder.textView4.setText("주소 : " + job.getData()[3]);
            holder.textView5.setText("시작일자 : " + job.getData()[4]);
            holder.textView6.setText("마감일자 : " + job.getData()[5]);
            holder.checkBox.setChecked(job.isChecked());
            holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> job.setChecked(isChecked));
        }

        public int getItemCount() {
            return jobDataList.size();
        }
    }
}
