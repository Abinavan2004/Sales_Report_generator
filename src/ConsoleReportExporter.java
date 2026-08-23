
    public class ConsoleReportExporter implements ReportExporter {
        @Override
        public void export(String content) {
            System.out.print(content);
        }
    }


