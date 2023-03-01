package com.pengxh.kt.lib;

import java.util.List;

public class SampleListModel {

    private int code;
    private DataModel data;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataModel getData() {
        return data;
    }

    public void setData(DataModel data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class DataModel {
        private List<RowsModel> rows;
        private int total;

        public List<RowsModel> getRows() {
            return rows;
        }

        public void setRows(List<RowsModel> rows) {
            this.rows = rows;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public static class RowsModel {
            private String certificationStatus;
            private String customerName;
            private String customerNo;
            private String id;
            private String manufacturingNo;
            private String measureLastTime;
            private String measurePeriod;
            private String orderId;
            private String remark;
            private String sampleModel;
            private String sampleName;
            private String sampleNo;
            private String sampleSatus;
            private String sampleSatusName;
            private String validDeadline;

            public String getCertificationStatus() {
                return certificationStatus;
            }

            public void setCertificationStatus(String certificationStatus) {
                this.certificationStatus = certificationStatus;
            }

            public String getCustomerName() {
                return customerName;
            }

            public void setCustomerName(String customerName) {
                this.customerName = customerName;
            }

            public String getCustomerNo() {
                return customerNo;
            }

            public void setCustomerNo(String customerNo) {
                this.customerNo = customerNo;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getManufacturingNo() {
                return manufacturingNo;
            }

            public void setManufacturingNo(String manufacturingNo) {
                this.manufacturingNo = manufacturingNo;
            }

            public String getMeasureLastTime() {
                return measureLastTime;
            }

            public void setMeasureLastTime(String measureLastTime) {
                this.measureLastTime = measureLastTime;
            }

            public String getMeasurePeriod() {
                return measurePeriod;
            }

            public void setMeasurePeriod(String measurePeriod) {
                this.measurePeriod = measurePeriod;
            }

            public String getOrderId() {
                return orderId;
            }

            public void setOrderId(String orderId) {
                this.orderId = orderId;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public String getSampleModel() {
                return sampleModel;
            }

            public void setSampleModel(String sampleModel) {
                this.sampleModel = sampleModel;
            }

            public String getSampleName() {
                return sampleName;
            }

            public void setSampleName(String sampleName) {
                this.sampleName = sampleName;
            }

            public String getSampleNo() {
                return sampleNo;
            }

            public void setSampleNo(String sampleNo) {
                this.sampleNo = sampleNo;
            }

            public String getSampleSatus() {
                return sampleSatus;
            }

            public void setSampleSatus(String sampleSatus) {
                this.sampleSatus = sampleSatus;
            }

            public String getSampleSatusName() {
                return sampleSatusName;
            }

            public void setSampleSatusName(String sampleSatusName) {
                this.sampleSatusName = sampleSatusName;
            }

            public String getValidDeadline() {
                return validDeadline;
            }

            public void setValidDeadline(String validDeadline) {
                this.validDeadline = validDeadline;
            }
        }
    }
}
