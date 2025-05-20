<template>
  <div class="user-info-container">
    <div>
      <div class="filter-container">
        <div class="left-items" >
          <el-button type="primary" icon="el-icon-edit" @click="addTo()">新增</el-button>
          <el-button type="primary" icon="el-icon-delete" @click="delBatch()">删除</el-button>
          <el-button type="primary" icon="el-icon-upload2" @click="uploadTo()">导入</el-button>
          <el-button type="primary" icon="el-icon-download" @click="download()">导出</el-button>
        </div>
        <div class="right-items" >
          <el-input placeholder="用户ID/用户名/手机号" v-model="param.queryName" style="width: 200px;margin-right: 10px;" />
          <el-button type="primary" icon="el-icon-search" @click="query()">查询</el-button>
          <el-button type="primary" icon="el-icon-refresh-left" @click="reset()">重置</el-button>
        </div>
      </div>
    </div>

    <div class="table-container">
      <!--项目列表展示-->
      <el-table class="main-table" ref="table" :data="tableData" @selection-change="handleSelectionChange" border fit
                :max-height="tableHeight" :header-cell-style="tableHeaderStyle" :cell-style="tableCellStyle" >
        <el-table-column type="selection" fixed ></el-table-column>
<!--         <el-table-column prop="userId" label="用户ID" min-width="150px"></el-table-column>-->
         <el-table-column prop="userName" label="用户名" min-width="150px" show-overflow-tooltip></el-table-column>
         <el-table-column prop="nickname" label="昵称" min-width="150px" show-overflow-tooltip></el-table-column>
         <el-table-column prop="userForeignName" label="外文名" min-width="150px" show-overflow-tooltip></el-table-column>
         <el-table-column prop="sex" label="性别" min-width="150px" show-overflow-tooltip></el-table-column>
         <el-table-column prop="birthday" label="出生日期" min-width="150px" show-overflow-tooltip></el-table-column>
         <el-table-column prop="phoneNo" label="手机号" min-width="150px" show-overflow-tooltip></el-table-column>
         <el-table-column prop="email" label="电子邮箱" min-width="150px" show-overflow-tooltip></el-table-column>
         <el-table-column prop="address" label="地址" min-width="150px" show-overflow-tooltip></el-table-column>
         <el-table-column prop="avatar" label="头像" min-width="150px" show-overflow-tooltip></el-table-column>
         <el-table-column prop="userStatus" label="用户状态" min-width="150px" show-overflow-tooltip></el-table-column>
         <el-table-column prop="remark" label="备注" min-width="150px" show-overflow-tooltip></el-table-column>
        <el-table-column label="操作" fixed="right" width="100">
          <template slot-scope="scope">
            <el-button size="mini" type="success" @click="updTo(scope.row)">编辑</el-button>
            <!--<el-button size="mini" type="danger" @click="handDel(scope.row)">Delete</el-button>-->
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        class="pagination"
        @size-change="handSizeChange"
        @current-change="handCurrentChange"
        :current-page="param.pageNum"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="param.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
      </el-pagination>
    </div>

      <!-- 新增/编辑对话框 -->
      <el-dialog
        :title="dialogTitle"
        :visible.sync="dialogVisible"
        width="55%"
        class="custom-dialog"
        :before-close="handleDialogClose">
        <el-form ref="form" :model="form" :rules="rules" label-width="100px">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="用户名" prop="userName">
                <el-input v-model="form.userName" placeholder="请输入用户名"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="昵称" prop="nickname">
                <el-input v-model="form.nickname" placeholder="请输入昵称"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="性别" prop="sex">
                <el-select v-model="form.sex" placeholder="请选择性别" class="full-width">
                  <el-option
                    v-for="item in sexOptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="出生日期" prop="birthday">
                <el-date-picker
                  v-model="form.birthday"
                  type="date"
                  placeholder="选择日期"
                  class="full-width">
                </el-date-picker>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="手机号" prop="phoneNo">
                <el-input v-model="form.phoneNo" placeholder="请输入手机号"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="邮箱" prop="email">
                <el-input v-model="form.email" placeholder="请输入邮箱"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="用户状态" prop="userStatus">
                <el-select v-model="form.userStatus" placeholder="请选择状态" class="full-width">
                  <el-option
                    v-for="item in statusOptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="地址" prop="address">
                <el-input v-model="form.address" placeholder="请输入地址"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item label="备注" prop="remark">
            <el-input type="textarea" v-model="form.remark" placeholder="请输入备注"/>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="handleSubmit">确 定</el-button>
        </div>
      </el-dialog>

  </div>
</template>

<script>
export default {
  name: 'UserInfoMain',
  data () {
    return {
      tableHeight: 500,
      titleName: '用户信息',
      param: {
        queryName: '',
        pageNum: 1, // 初始页
        pageSize: 10 // 每页的数据
      },
      total: 0,
      tableData: [],
      multipleSelection: [],
      dialogVisible: false,
      dialogType: 'add', // add or edit
      form: {},
      rules: {
        userName: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        phoneNo: [{ required: true, message: '请输入手机号', trigger: 'blur' }],
        email: [{ required: true, message: '请输入邮箱', trigger: 'blur' }]
      },
      sexOptions: [
        { value: 'M', label: '男' },
        { value: 'F', label: '女' }
      ],
      statusOptions: [
        { value: 'Y', label: '启用' },
        { value: 'N', label: '禁用' }
      ],
      tableHeaderStyle: {
        'text-align': 'center'
      },
      tableCellStyle: {
        'text-align': 'center'
      }
    }
  },
  computed: {
    dialogTitle() {
      return this.dialogType === 'add' ? '新增用户' : '编辑用户'
    }
  },
  mounted() {
    this.initTableHeight()
    window.addEventListener('resize', this.handleResize)
    this.init()
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.handleResize)
  },
  methods: {
    initTableHeight() {
      this.$nextTick(() => {
        this.tableHeight = window.innerHeight - 280
      })
    },
    handleResize() {
      this.tableHeight = window.innerHeight - 280
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    handSizeChange(size) {
      this.param.pageSize = size
      this.init()
    },
    handCurrentChange(page) {
      this.param.pageNum = page
      this.init()
    },
    init() {
      // 调用API获取数据
      this.$axios.post('/api/web/userInfo/query', this.param).then(res => {
        if (res.data.code === '100200') {
          this.tableData = res.data.object.list
          this.total = res.data.object.total
        } else {
          this.$message.error(res.data.msg)
        }
      })
    },
    query() {
      this.param.pageNum = 1
      this.init()
    },
    reset() {
      this.param.queryName = ''
      this.query()
    },
    addTo() {
      this.dialogType = 'add'
      this.form = {}
      this.dialogVisible = true
    },
    updTo(row) {
      this.dialogType = 'edit'
      this.form = { ...row }
      this.dialogVisible = true
    },
    handleDialogClose(done) {
      this.$refs.form.resetFields()
      done()
    },
    handleSubmit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          const url = this.dialogType === 'add' ? '/api/web/userInfo/add' : '/api/web/userInfo/update'
          this.$axios.post(url, this.form).then(res => {
            if (res.data.code === '100200') {
              this.$message.success('操作成功')
              this.dialogVisible = false
              this.init()
            } else {
              this.$message.error(res.data.msg)
            }
          })
        }
      })
    },
    delBatch() {
      if (this.multipleSelection.length === 0) {
        this.$message.warning('请选择要删除的记录')
        return
      }
      this.$confirm('确认删除选中记录?', '提示', {
        type: 'warning'
      }).then(() => {
        const ids = this.multipleSelection.map(item => item.id)
        this.$axios.post('/api/web/userInfo/delete', { ids }).then(res => {
          if (res.data.code === '100200') {
            this.$message.success('删除成功')
            this.init()
          } else {
            this.$message.error(res.data.msg)
          }
        })
      })
    }



  }
}
</script>

<style scoped>
.user-info-container {
  background-color: #f0f2f5;
  min-height: calc(100vh - 100px);
}

.filter-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
  margin-bottom: 16px;
  background: #fff;
  border-radius: 4px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.1);
}

.search-input {
  width: 200px;
  margin-right: 10px;
}

.table-container {
  background: #fff;
  border-radius: 4px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.1);
}

.pagination {
  text-align: right;
  margin-top: 8px;  /* 从16px减小到8px */
  padding: 0;  /* 确保没有额外的内边距 */
  line-height: 1;  /* 确保行高紧凑 */
}
/* 分页器组件样式调整 */
:deep(.el-pagination) {
  padding: 0;  /* 移除默认内边距 */
  margin: 0;   /* 移除默认外边距 */
  font-size: 13px;  /* 稍微减小字体大小 */
}

/* 分页器按钮样式调整 */
:deep(.el-pagination .el-pager li) {
  margin: 0 2px;  /* 减小页码按钮之间的间距 */
}

.full-width {
  width: 100%;
}

/* 按钮间距 */
.left-items .el-button + .el-button,
.right-items .el-button + .el-button {
  margin-left: 8px;
}

/* 表格hover效果 */
:deep(.el-table--enable-row-hover .el-table__body tr:hover > td) {
  background-color: #f5f7fa;
}

/* 对话框样式 */
.custom-dialog {
  :deep(.el-dialog__body) {
    padding: 20px 30px;
  }
}

@media screen and (max-width: 768px) {
  .filter-container {
    flex-direction: column;
    align-items: stretch;
  }

  .left-items, .right-items {
    margin: 8px 0;
  }

  .search-input {
    width: 100%;
    margin-bottom: 8px;
  }
}
</style>
