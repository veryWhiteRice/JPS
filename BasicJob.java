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


public class BasicJob extends AppCompatActivity {

    private RecyclerView recyclerView;
    private JobAdapter jobAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basic_job);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Start AsyncTask to read CSV file
        new ReadCSVFileTask().execute("장애인구직정보_전처리.csv");
    }

    private class ReadCSVFileTask extends AsyncTask<String, Void, List<JobData>> {

        @Override
        protected List<JobData> doInBackground(String... filenames) {
            List<JobData> jobDataList = new ArrayList<>();
            try {
                InputStreamReader inputStreamReader = new InputStreamReader(getAssets().open(filenames[0]), "UTF-8");
                CSVReader csvReader = new CSVReader(inputStreamReader);

                String[] nextLine;

                // 첫 번째 행(헤더) 읽고 무시
                try {
                    csvReader.readNext();
                } catch (CsvValidationException e) {
                    e.printStackTrace();
                }

                // 이후 행들 읽기
                try {
                    while ((nextLine = csvReader.readNext()) != null) {
                        JobData jobData = new JobData(nextLine);
                        jobDataList.add(jobData);
                    }
                } catch (CsvValidationException e) {
                    e.printStackTrace();
                }

                csvReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return jobDataList;
        }

        @Override
        protected void onPostExecute(List<JobData> jobDataList) {
            jobAdapter = new JobAdapter(jobDataList);
            recyclerView.setAdapter(jobAdapter);
        }
    }

    private List<String[]> readCSVFileFromAssets(String fileName) throws IOException {
        List<String[]> data = new ArrayList<>();

        InputStreamReader inputStreamReader = new InputStreamReader(getAssets().open(fileName), "UTF-8");
        CSVReader csvReader = new CSVReader(inputStreamReader);

        String[] nextLine;

        // 첫 번째 행(헤더) 읽고 무시
        try {
            csvReader.readNext();
        } catch (CsvValidationException e) {
            e.printStackTrace();
        }

        // 이후 행들 읽기
        try {
            while ((nextLine = csvReader.readNext()) != null) {
                data.add(nextLine);
            }
        } catch (CsvValidationException e) {
            e.printStackTrace();
        }

        csvReader.close();

        return data;
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
        private final List<JobData> jobDataList;

        JobAdapter(List<JobData> jobDataList) {
            this.jobDataList = jobDataList;
        }

        @Override
        public JobViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
            return new JobViewHolder(view);
        }

        @Override
        public void onBindViewHolder(JobViewHolder holder, int position) {
            JobData job = jobDataList.get(position);
            String[] row = job.getData();
            holder.textView1.setText("회사명 : " + row[1]);
            holder.textView2.setText("직종 업무 : " + row[2]);
            holder.textView3.setText("계약 구분 : " + row[3]);
            holder.textView4.setText("주소 : " + row[11]);
            holder.textView5.setText("시작일자 : " + row[20]);
            holder.textView6.setText("마감일자 : " + row[21]);

            // 체크박스 설정
            holder.checkBox.setChecked(job.isChecked());
            holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> job.setChecked(isChecked));
        }

        @Override
        public int getItemCount() {
            return jobDataList.size();
        }
    }
}
