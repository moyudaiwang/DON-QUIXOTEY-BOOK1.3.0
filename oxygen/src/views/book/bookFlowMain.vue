<template>
  <div class="main-app">
    <div>
      <div class="filter-container">
        <div class="letf-items">
          <el-button type="primary" icon="el-icon-edit" @click="insTo()">新增</el-button>
          <el-button type="primary" icon="el-icon-delete" @click="delBatch()">删除</el-button>
<!--  TODO 1.2.0上线前隐藏        <el-button type="primary" icon="el-icon-upload2" @click="uploadExcel()">导入</el-button>
          <el-button type="primary" icon="el-icon-download" @click="downloadExcel()">导出</el-button>-->
        </div>
        <div class="right-items">
          <el-input placeholder="请输入内容" v-model="param.queryName" clearable class="input-with-select"></el-input>
          <el-button  type="primary" icon="el-icon-search" @click="query()">查询</el-button>
<!--TODO 1.2.0上线前隐藏
          <el-button  type="primary" icon="el-icon-refresh-left" @click="reset()">重置</el-button>
-->
        </div>
      </div>
    </div>

    <div>
      <!--项目列表展示-->
      <el-table  :data="tableData" @selection-change="handleSelectionChange" border fit height="520px" :header-cell-style="{'text-align':'center'}" :cell-style="{'text-align':'center'}" header-align="center">
        <el-table-column type="selection" fixed ></el-table-column>
        <el-table-column type="expand">
          <template slot-scope="props">
            <el-form label-position="left" inline class="demo-table-expand">
              <el-form-item label="ISBN_PID">
                <span>{{ props.row.isbnPid }}</span>
              </el-form-item>
              <el-form-item label="ISBN_SID">
                <span>{{ props.row.isbnSid }}</span>
              </el-form-item>
              <el-form-item label="外文名">
                <span>{{ props.row.bookForeignName }}</span>
              </el-form-item>
              <el-form-item label="愿景时间">
                <span>{{ formatDate(props.row, {property: 'visionDate'}) }}</span>
              </el-form-item>
              <el-form-item label="愿景人">
                <span>{{ props.row.visionMan }}</span>
              </el-form-item>
              <el-form-item label="愿景分组">
                <span>{{ props.row.visionGroup }}</span>
              </el-form-item>
              <el-form-item label="购书人">
                <span>{{ props.row.shopMan }}</span>
              </el-form-item>
              <el-form-item label="购书书品">
                <span>{{ props.row.shopGrade }}</span>
              </el-form-item>
              <el-form-item label="购书平台">
                <span>{{ props.row.shopPlatform }}</span>
              </el-form-item>
              <el-form-item label="购书单位">
                <span>{{ props.row.shopUnit }}</span>
              </el-form-item>
              <el-form-item label="购书目的">
                <span>{{ props.row.shopAim }}</span>
              </el-form-item>
              <el-form-item label="架位编号">
                <span>{{ props.row.rackNum }}</span>
              </el-form-item>
              <el-form-item label="收藏人">
                <span>{{ props.row.collector }}</span>
              </el-form-item>
              <el-form-item label="出藏时间">
                <span>{{ formatDate(props.row, {property: 'exportDate'}) }}</span>
              </el-form-item>
              <el-form-item label="出藏书品">
                <span>{{ props.row.exportGrade }}</span>
              </el-form-item>
              <el-form-item label="预计回藏地点">
                <span>{{ props.row.planHomewardAddress }}</span>
              </el-form-item>
              <el-form-item label="回藏地点">
                <span>{{ props.row.homewardAddress }}</span>
              </el-form-item>
              <el-form-item label="出藏员">
                <span>{{ props.row.exportMan }}</span>
              </el-form-item>
              <el-form-item label="出藏价格">
                <span>{{ props.row.exportPrice }}</span>
              </el-form-item>
              <el-form-item label="接收法人">
                <span>{{ props.row.receivingUnit }}</span>
              </el-form-item>
              <el-form-item label="预计回藏时间">
                <span>{{ formatDate(props.row, {property: 'planHomewardDate'}) }}</span>
              </el-form-item>
              <el-form-item label="回藏时间">
                <span>{{ formatDate(props.row, {property: 'homewardDate'}) }}</span>
              </el-form-item>
              <el-form-item label="回藏员">
                <span>{{ props.row.homewardMan }}</span>
              </el-form-item>
              <el-form-item label="备注">
                <span>{{ props.row.remark }}</span>
              </el-form-item>
            </el-form>
          </template>
        </el-table-column>
         <el-table-column prop="bookId" label="图书ID" min-width="150px"></el-table-column>
         <el-table-column prop="isbn" label="ISBN" min-width="150px"></el-table-column>
         <el-table-column prop="bookName" label="中文名" min-width="150px"></el-table-column>
         <el-table-column prop="shopPrice" label="购书价格" min-width="150px"></el-table-column>
         <el-table-column prop="shopAddress" label="购书地点" min-width="150px"></el-table-column>
         <el-table-column prop="shopDate" label="购书时间" min-width="150px" :formatter="formatDate"></el-table-column>
         <el-table-column prop="collectionAddress" label="藏书地点" min-width="150px"></el-table-column>
         <el-table-column prop="bookStatus" label="图书状态" min-width="150px"></el-table-column>
        <el-table-column label="操作" fixed="right" width="100px">
          <template slot-scope="scope">
            <el-button size="mini" type="success" @click="updTo(scope.row)">编辑</el-button>
            <!--<el-button size="mini" type="danger" @click="handDel(scope.row)">Delete</el-button>-->
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        @size-change="handSizeChange"
        @current-change="handCurrentChange"
        :current-page="param.pageNum"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="param.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
      </el-pagination>
    </div>

    <div>
      <el-dialog :title="'新增'+titleName" :visible.sync="insVisible" width="55%">
        <el-form ref="insRef" :model="insForm" :rules="insRules" label-position="right" label-width="100px">
            <el-row>
                <el-col :span="10">
                    <el-form-item label="图书ID" prop="bookId">
                        <el-input v-model="insForm.bookId" placeholder="图书ID"/>
                    </el-form-item>
                </el-col>
                <el-col :span="10">
                    <el-form-item label="ISBN" prop="isbn">
                        <el-input v-model="insForm.isbn" placeholder="ISBN"/>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="10">
                    <el-form-item label="ISBN_PID" prop="isbnPid">
                        <el-input v-model="insForm.isbnPid" placeholder="ISBN_PID"/>
                    </el-form-item>
                </el-col>
                <el-col :span="10">
                    <el-form-item label="ISBN_SID" prop="isbnSid">
                        <el-input v-model="insForm.isbnSid" placeholder="ISBN_SID"/>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="10">
                    <el-form-item label="中文名" prop="bookName">
                        <el-input v-model="insForm.bookName" placeholder="中文名"/>
                    </el-form-item>
                </el-col>
                <el-col :span="10">
                    <el-form-item label="外文名" prop="bookForeignName">
                        <el-input v-model="insForm.bookForeignName" placeholder="外文名"/>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="10">
                    <el-form-item label="愿景时间" prop="visionDate">
                        <el-date-picker v-model="insForm.visionDate" placeholder="愿景时间" type="date"/>
                    </el-form-item>
                </el-col>
                <el-col :span="10">
                    <el-form-item label="愿景人" prop="visionMan">
                        <el-input v-model="insForm.visionMan" placeholder="愿景人"/>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="10">
                    <el-form-item label="愿景分组" prop="visionGroup">
                        <el-input v-model="insForm.visionGroup" placeholder="愿景分组"/>
                    </el-form-item>
                </el-col>
                <el-col :span="10">
                    <el-form-item label="购书价格" prop="shopPrice">
                        <el-input v-model="insForm.shopPrice" placeholder="购书价格"/>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="10">
                    <el-form-item label="购书书品" prop="shopGrade">
                        <el-input v-model="insForm.shopGrade" placeholder="购书书品"/>
                    </el-form-item>
                </el-col>
                <el-col :span="10">
                    <el-form-item label="购书人" prop="shopMan">
                        <el-input v-model="insForm.shopMan" placeholder="购书人"/>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="10">
                    <el-form-item label="购书平台" prop="shopPlatform">
                        <el-input v-model="insForm.shopPlatform" placeholder="购书平台"/>
                    </el-form-item>
                </el-col>
                <el-col :span="10">
                    <el-form-item label="购书地点" prop="shopAddress">
                        <el-input v-model="insForm.shopAddress" placeholder="购书地点"/>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="10">
                    <el-form-item label="购书单位" prop="shopUnit">
                        <el-input v-model="insForm.shopUnit" placeholder="购书单位"/>
                    </el-form-item>
                </el-col>
                <el-col :span="10">
                    <el-form-item label="购书时间" prop="shopDate">
                        <el-input v-model="insForm.shopDate" placeholder="购书时间"/>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="10">
                    <el-form-item label="购书目的" prop="shopAim">
                        <el-input v-model="insForm.shopAim" placeholder="购书目的"/>
                    </el-form-item>
                </el-col>
                <el-col :span="10">
                    <el-form-item label="收藏人" prop="collector">
                        <el-input v-model="insForm.collector" placeholder="收藏人"/>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="10">
                    <el-form-item label="藏书地点" prop="collectionAddress">
                        <el-input v-model="insForm.collectionAddress" placeholder="藏书地点"/>
                    </el-form-item>
                </el-col>
                <el-col :span="10">
                    <el-form-item label="架位编号" prop="rackNum">
                        <el-input v-model="insForm.rackNum" placeholder="架位编号"/>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="10">
                    <el-form-item label="出藏时间" prop="exportDate">
                        <el-input v-model="insForm.exportDate" placeholder="出藏时间"/>
                    </el-form-item>
                </el-col>
                <el-col :span="10">
                    <el-form-item label="出藏员" prop="exportMan">
                        <el-input v-model="insForm.exportMan" placeholder="出藏员"/>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="10">
                    <el-form-item label="出藏价格" prop="exportPrice">
                        <el-input v-model="insForm.exportPrice" placeholder="出藏价格"/>
                    </el-form-item>
                </el-col>
                <el-col :span="10">
                    <el-form-item label="出藏书品" prop="exportGrade">
                        <el-input v-model="insForm.exportGrade" placeholder="出藏书品"/>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="10">
                    <el-form-item label="接收法人" prop="receivingUnit">
                        <el-input v-model="insForm.receivingUnit" placeholder="接收法人"/>
                    </el-form-item>
                </el-col>
                <el-col :span="10">
                    <el-form-item label="预计回藏时间" prop="planHomewardDate">
                        <el-input v-model="insForm.planHomewardDate" placeholder="预计回藏时间"/>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="10">
                    <el-form-item label="预计回藏地点" prop="planHomewardAddress">
                        <el-input v-model="insForm.planHomewardAddress" placeholder="预计回藏地点"/>
                    </el-form-item>
                </el-col>
                <el-col :span="10">
                    <el-form-item label="回藏时间" prop="homewardDate">
                        <el-input v-model="insForm.homewardDate" placeholder="回藏时间"/>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="10">
                    <el-form-item label="回藏地点" prop="homewardAddress">
                        <el-input v-model="insForm.homewardAddress" placeholder="回藏地点"/>
                    </el-form-item>
                </el-col>
                <el-col :span="10">
                    <el-form-item label="回藏员" prop="homewardMan">
                        <el-input v-model="insForm.homewardMan" placeholder="回藏员"/>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="10">
                    <el-form-item label="图书状态" prop="bookStatus">
                        <el-input v-model="insForm.bookStatus" placeholder="图书状态"/>
                    </el-form-item>
                </el-col>
                <el-col :span="10">
                    <el-form-item label="备注" prop="remark">
                        <el-input v-model="insForm.remark" placeholder="备注"/>
                    </el-form-item>
                </el-col>
            </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="insVisible = false">取消</el-button>
          <el-button type="primary" @click="ins()">确定</el-button>
        </div>
      </el-dialog>
    </div>

    <div>
      <el-dialog :title="'修改'+titleName" :visible.sync="updVisible" width="55%">
        <el-form ref="updRef" :model="updForm" :rules="updRules" label-position="right" label-width="100px">
            <el-row>
                <el-col :span="10">
                    <el-form-item label="图书ID" prop="bookId">
                        <el-input v-model="updForm.bookId" placeholder="图书ID"/>
                    </el-form-item>
                </el-col>
                <el-col :span="10">
                    <el-form-item label="ISBN" prop="isbn">
                        <el-input v-model="updForm.isbn" placeholder="ISBN"/>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="10">
                    <el-form-item label="ISBN_PID" prop="isbnPid">
                        <el-input v-model="updForm.isbnPid" placeholder="ISBN_PID"/>
                    </el-form-item>
                </el-col>
                <el-col :span="10">
                    <el-form-item label="ISBN_SID" prop="isbnSid">
                        <el-input v-model="updForm.isbnSid" placeholder="ISBN_SID"/>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="10">
                    <el-form-item label="中文名" prop="bookName">
                        <el-input v-model="updForm.bookName" placeholder="中文名"/>
                    </el-form-item>
                </el-col>
                <el-col :span="10">
                    <el-form-item label="外文名" prop="bookForeignName">
                        <el-input v-model="updForm.bookForeignName" placeholder="外文名"/>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="10">
                    <el-form-item label="愿景时间" prop="visionDate">
                      <el-date-picker v-model="updForm.visionDate" placeholder="愿景时间" type="date"/>
                    </el-form-item>
                </el-col>
                <el-col :span="10">
                    <el-form-item label="愿景人" prop="visionMan">
                        <el-input v-model="updForm.visionMan" placeholder="愿景人"/>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="10">
                    <el-form-item label="愿景分组" prop="visionGroup">
                        <el-input v-model="updForm.visionGroup" placeholder="愿景分组"/>
                    </el-form-item>
                </el-col>
                <el-col :span="10">
                    <el-form-item label="购书价格" prop="shopPrice">
                        <el-input v-model="updForm.shopPrice" placeholder="购书价格"/>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="10">
                    <el-form-item label="购书书品" prop="shopGrade">
                        <el-input v-model="updForm.shopGrade" placeholder="购书书品"/>
                    </el-form-item>
                </el-col>
                <el-col :span="10">
                    <el-form-item label="购书人" prop="shopMan">
                        <el-input v-model="updForm.shopMan" placeholder="购书人"/>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="10">
                    <el-form-item label="购书平台" prop="shopPlatform">
                        <el-input v-model="updForm.shopPlatform" placeholder="购书平台"/>
                    </el-form-item>
                </el-col>
                <el-col :span="10">
                    <el-form-item label="购书地点" prop="shopAddress">
                        <el-input v-model="updForm.shopAddress" placeholder="购书地点"/>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="10">
                    <el-form-item label="购书单位" prop="shopUnit">
                        <el-input v-model="updForm.shopUnit" placeholder="购书单位"/>
                    </el-form-item>
                </el-col>
                <el-col :span="10">
                    <el-form-item label="购书时间" prop="shopDate">
                        <el-input v-model="updForm.shopDate" placeholder="购书时间"/>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="10">
                    <el-form-item label="购书目的" prop="shopAim">
                        <el-input v-model="updForm.shopAim" placeholder="购书目的"/>
                    </el-form-item>
                </el-col>
                <el-col :span="10">
                    <el-form-item label="收藏人" prop="collector">
                        <el-input v-model="updForm.collector" placeholder="收藏人"/>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="10">
                    <el-form-item label="藏书地点" prop="collectionAddress">
                        <el-input v-model="updForm.collectionAddress" placeholder="藏书地点"/>
                    </el-form-item>
                </el-col>
                <el-col :span="10">
                    <el-form-item label="架位编号" prop="rackNum">
                        <el-input v-model="updForm.rackNum" placeholder="架位编号"/>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="10">
                    <el-form-item label="出藏时间" prop="exportDate">
                        <el-input v-model="updForm.exportDate" placeholder="出藏时间"/>
                    </el-form-item>
                </el-col>
                <el-col :span="10">
                    <el-form-item label="出藏员" prop="exportMan">
                        <el-input v-model="updForm.exportMan" placeholder="出藏员"/>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="10">
                    <el-form-item label="出藏价格" prop="exportPrice">
                        <el-input v-model="updForm.exportPrice" placeholder="出藏价格"/>
                    </el-form-item>
                </el-col>
                <el-col :span="10">
                    <el-form-item label="出藏书品" prop="exportGrade">
                        <el-input v-model="updForm.exportGrade" placeholder="出藏书品"/>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="10">
                    <el-form-item label="接收法人" prop="receivingUnit">
                        <el-input v-model="updForm.receivingUnit" placeholder="接收法人"/>
                    </el-form-item>
                </el-col>
                <el-col :span="10">
                    <el-form-item label="预计回藏时间" prop="planHomewardDate">
                        <el-input v-model="updForm.planHomewardDate" placeholder="预计回藏时间"/>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="10">
                    <el-form-item label="预计回藏地点" prop="planHomewardAddress">
                        <el-input v-model="updForm.planHomewardAddress" placeholder="预计回藏地点"/>
                    </el-form-item>
                </el-col>
                <el-col :span="10">
                    <el-form-item label="回藏时间" prop="homewardDate">
                        <el-input v-model="updForm.homewardDate" placeholder="回藏时间"/>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="10">
                    <el-form-item label="回藏地点" prop="homewardAddress">
                        <el-input v-model="updForm.homewardAddress" placeholder="回藏地点"/>
                    </el-form-item>
                </el-col>
                <el-col :span="10">
                    <el-form-item label="回藏员" prop="homewardMan">
                        <el-input v-model="updForm.homewardMan" placeholder="回藏员"/>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="10">
                    <el-form-item label="图书状态" prop="bookStatus">
                        <el-input v-model="updForm.bookStatus" placeholder="图书状态"/>
                    </el-form-item>
                </el-col>
                <el-col :span="10">
                    <el-form-item label="备注" prop="remark">
                        <el-input v-model="updForm.remark" placeholder="备注"/>
                    </el-form-item>
                </el-col>
            </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="updVisible = false">取消</el-button>
          <el-button type="primary" @click="upd()">确定</el-button>
        </div>
      </el-dialog>
    </div>

  </div>
</template>

<script>
export default {
  name: 'logMain',
  data () {
    return {
      titleName: '图书流动信息',
      param: {
        queryName: '',
        pageNum: 1, // 初始页
        pageSize: 10 // 每页的数据
      },
      total: 0,
      multipleSelection: [],
      tableData: [],
      insVisible: false,
      insRules: {},
      insForm: {},
      updVisible: false,
      updRules: {},
      updForm: {},
      userStatusOptions: [
        {label: '正常', value: 'Y'}
      ],
      userStatus: true
    }
  },
  mounted () {
    this.init()
  },
  methods: {
    handleSelectionChange (val) {
      this.multipleSelection = val
    },
    handSizeChange: function (size) {
      this.param.pageSize = size
      this.init()
    },
    handCurrentChange: function (pageNum) {
      this.param.pageNum = pageNum
      this.init()
    },
    // 初始化
    init () {
      let donBookFlowEntity = {
        queryName: this.param.queryName,
        pageNum: this.param.pageNum,
        pageSize: this.param.pageSize
      }
      var url = '/api/web/bookFlow/query'
      this.$axios.post(url, donBookFlowEntity).then(res => {
        if (res.data.code == '100200') {
          this.tableData = res.data.object.list
          this.total = res.data.object.total
        } else {
          this.$message({message: res.data.msg, type: 'error'})
        }
      }).catch(error => {
        console.log(error)
      })
    },
    // 查询
    query () {
      this.init()
    },
    // 重置
    reset () {
      this.param.queryName = ''
      this.init()
    },
    // 新增TO
    insTo () {
      this.insVisible = true
    },
    // 新增
    ins () {
      let donBookFlowEntity = this.insForm
      var url = '/api/web/bookFlow/insert'
      this.$axios.post(url, donBookFlowEntity).then(res => {
        if (res.data.code == '100200') {
          this.insVisible = false
          this.insForm = {}
          this.init()
          this.$message({message: res.data.msg, type: 'success', center: true, duration: 2000})
        } else {
          this.insVisible = false
          this.$message({message: res.data.msg, type: 'error', center: true, duration: 2000})
        }
      }).catch(error => {
        console.log(error)
      })
    },
    // 修改TO
    updTo (row) {
      this.updForm = row
      this.updVisible = true
    },
    // 修改
    upd () {
      let donBookFlowEntity = this.updForm
      var url = '/api/web/bookFlow/update'
      this.$axios.post(url, donBookFlowEntity).then(res => {
        if (res.data.code == '100200') {
          this.updVisible = false
          this.init()
          this.$message({message: res.data.msg, type: 'success', center: true, duration: 2000})
        } else {
          this.updVisible = false
          this.$message({message: res.data.msg, type: 'error', center: true, duration: 2000})
        }
      }).catch(error => {
        console.log(error)
      })
    },
    // 批量删除
    delBatch () {
      if (this.multipleSelection.length != 0) {
        this.$confirm('请确认是否删除?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          let donUserInfoEntityList = this.multipleSelection
          var url = '/api/web/bookFlow/delete'
          this.$axios.post(url, donUserInfoEntityList).then(res => {
            if (res.data.code == '100200') {
              this.param = ''
              this.init()
              this.$message({message: res.data.msg, type: 'success', center: true, duration: 2000})
            } else {
              this.param = ''
              this.init()
              this.$message({message: res.data.msg, type: 'error', center: true, duration: 2000})
            }
          }).catch(error => {
            console.log(error)
          })
        }).catch(error => {
          console.log(error)
        })
      } else {
        this.$message({message: '请选择要删除的数据', type: 'warning', center: true, duration: 2000})
      }
    },
    // 导入
    uploadExcel () {
    },
    // 导出
    downloadExcel () {
    },
    // 格式化日期
    formatDate(row, column) {
      const value = row[column.property];
      if (value) {
        const date = new Date(value);
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        return `${year}-${month}-${day}`;
      }
      return '';
    }
  }

}
</script>

<style>
  /*表头高度 */
  .el-table__header tr, .el-table__header th {
    padding: 0;
    height: 40px;
    line-height: 50px;
  }
  .el-table__body tr, .el-table__body td {
    padding: 0;
    height: 40px;
    line-height: 40px;
  }
  /*表格与表头线对齐 */
  .el-table th.gutter{
    display: table-cell!important;
  }

  /*输入框*/
  .el-input{
    width: 300px;
  }
  /*input-with-select宽度*/
  .input-with-select {
    width: 200px;
  }
  /*单选*/
  .radioCss {
    margin: 5px 0px 0px 0px;
    float: left;
  }
  /*按钮靠左对齐*/
  .letf-items {
    float: left;
  }
  /*搜索栏、按钮靠右对齐*/
  .right-items {
    float: right;
  }
  /*列表*/
  .el-table {
    width: 100%;
  }
  /*表单*/
  .el-form {
    margin-left:40px;
  }

  /* 展开行样式 */
  .demo-table-expand {
    font-size: 0;
  }
  .demo-table-expand label {
    width: 120px;
    color: #99a9bf;
  }
  .demo-table-expand .el-form-item {
    margin-right: 0;
    margin-bottom: 0;
    width: 33.33%;
  }
  .demo-table-expand .el-form-item__label {
    text-align: right;
    padding-right: 12px;
  }
  .demo-table-expand .el-form-item__content {
    font-size: 14px;
    margin-left: 8px;
  }

  /* 弹窗表单样式 */
  .el-dialog .el-form {
    margin: 0 auto;
  }
  .el-dialog .el-form-item {
    margin-bottom: 15px;
  }
  .el-dialog .el-form-item__label {
    width: 120px !important;
    text-align: right;
    padding-right: 12px;
  }
  .el-dialog .el-form-item__content {
    margin-left: 120px !important;
  }
  .el-dialog .el-input,
  .el-dialog .el-date-picker {
    width: 220px !important;
  }
  .el-dialog .el-row {
    margin-bottom: 5px;
  }
  .el-dialog .el-col {
    padding: 0 20px;
  }

</style>
