package com.pengxh.kt.lib

class NoticeListModel {
    var code = 0
    lateinit var data: DataModel
    lateinit var message: String

    class DataModel {
        lateinit var rows: MutableList<RowsModel>
        var total = 0

        class RowsModel {
            lateinit var createTime: String
            lateinit var id: String
            lateinit var isDel: String
            lateinit var minioFileName: String
            lateinit var noticeCompany: String
            lateinit var noticeContent: String
            lateinit var noticeNo: String
            lateinit var noticePublisher: String
            lateinit var noticeSketch: String
            lateinit var noticeTime: String
            lateinit var noticeTitle: String
            lateinit var read: String
            lateinit var updateTime: String
        }
    }
}