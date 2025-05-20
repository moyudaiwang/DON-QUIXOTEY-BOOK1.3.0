<template>
  <div class="calendar-container">
    <div class="calendar-header">
      <el-button @click="changeMonth(-1)">上个月</el-button>
      <span class="current-date">{{ currentYear }}年 {{ currentMonth + 1 }}月</span>
      <el-button @click="changeMonth(1)">下个月</el-button>
    </div>

    <div class="calendar-body">
      <div class="weekdays">
        <div v-for="day in weekDays" :key="day" class="weekday">{{ day }}</div>
      </div>

      <div class="days">
        <div v-for="day in calendarDays"
             :key="day.id"
             class="day-cell"
             :class="{ 'current-month': day.currentMonth, 'today': day.isToday }">
          <div class="solar-day">{{ day.dayOfMonth }}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Calendar',
  data() {
    return {
      currentDate: new Date(),
      currentYear: new Date().getFullYear(),
      currentMonth: new Date().getMonth(),
      weekDays: ['日', '一', '二', '三', '四', '五', '六'],
      calendarDays: []
    }
  },
  created() {
    this.generateCalendarDays();
  },
  methods: {
    generateCalendarDays() {
      const firstDay = new Date(this.currentYear, this.currentMonth, 1);
      const lastDay = new Date(this.currentYear, this.currentMonth + 1, 0);
      const daysInMonth = lastDay.getDate();
      const startingDay = firstDay.getDay();

      this.calendarDays = [];

      // 填充上个月的日期
      const prevMonth = new Date(this.currentYear, this.currentMonth - 1, 0);
      const prevMonthDays = prevMonth.getDate();

      for (let i = startingDay - 1; i >= 0; i--) {
        this.calendarDays.push({
          date: new Date(this.currentYear, this.currentMonth - 1, prevMonthDays - i),
          dayOfMonth: prevMonthDays - i,
          currentMonth: false,
          isToday: false,
          id: `${this.currentYear}-${(this.currentMonth - 1).toString().padStart(2, '0')}-${(prevMonthDays - i).toString().padStart(2, '0')}`
        });
      }

      // 当前月的日期
      const today = new Date();
      for (let i = 1; i <= daysInMonth; i++) {
        const date = new Date(this.currentYear, this.currentMonth, i);
        this.calendarDays.push({
          date,
          dayOfMonth: i,
          currentMonth: true,
          isToday: today.toDateString() === date.toDateString(),
          id: `${this.currentYear}-${this.currentMonth.toString().padStart(2, '0')}-${i.toString().padStart(2, '0')}`
        });
      }

      // 填充下个月的日期
      const remainingDays = 42 - this.calendarDays.length;
      for (let i = 1; i <= remainingDays; i++) {
        this.calendarDays.push({
          date: new Date(this.currentYear, this.currentMonth + 1, i),
          dayOfMonth: i,
          currentMonth: false,
          isToday: false,
          id: `${this.currentYear}-${(this.currentMonth + 1).toString().padStart(2, '0')}-${i.toString().padStart(2, '0')}`
        });
      }
    },

    changeMonth(offset) {
      const newDate = new Date(this.currentYear, this.currentMonth + offset, 1);
      this.currentYear = newDate.getFullYear();
      this.currentMonth = newDate.getMonth();
      this.generateCalendarDays();
    }
  }
}
</script>

<style scoped>
.calendar-container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 20px;
}

.calendar-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.current-date {
  font-size: 20px;
  font-weight: bold;
}

.weekdays {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  text-align: center;
  font-weight: bold;
  background-color: #f5f5f5;
  padding: 10px 0;
}

.days {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  gap: 1px;
  background-color: #eee;
}

.day-cell {
  min-height: 100px;
  padding: 5px;
  background-color: white;
  border: 1px solid #eee;
}

.day-cell.current-month {
  background-color: white;
}

.day-cell.today {
  background-color: #e6f7ff;
}

.solar-day {
  font-size: 16px;
  font-weight: bold;
}
</style>
