from xlwings import Book
from datetime import datetime
from docx import Document
from request_sheet import *

class timesheet_main:

    def __init__(self):
        self.file = Book('demo\\src\\main\\resources\\main.xls')
        self.timesheet = self.file.sheets['Sheet1']
        self.shift_details = "placeholder"
        self.columns = ['C', 'D', 'E', 'F', 'H', 'J', 'N', 'I']
        self.profile = dict()
        self.request_sheet_break = False
        self.breaktime = "placeholder"

    def setup_profile(self, details): #fix placements
        self.timesheet.range('K2').value = details["last_name"]
        self.timesheet.range('K4').value = details["first_name"]
        self.timesheet.range('F4').value = details["employee_number"]
        self.profile = details
        self.save_sheet()

    def set_month(self, month):
        self.timesheet.range('L7').value = month

    def set_year(self, year):
        self.timesheet.range('K7').value = year

    def current_date(self):
        self.timesheet.range('G58').value = datetime.now().day
        self.timesheet.range('H58').value = datetime.now().strftime('%B')
        self.timesheet.range('I58').value = datetime.now().year

    def sheet2sheet(self, file_path):
        doc = Document(file_path)
        request = request_sheet(doc)
        self.shift_details = request.collate_to_dict()
        print(self.shift_details)
        location = print(input("Confirm Shift Location:"))
        self.shift_details["location"] = location
        return self.shift_details

    def append_to_sheet(self):
        
        def find_cell(column, target):
            for i in range(13, 43):
                if self.timesheet.range(f'{column}{i}').value == target:
                    return i
            return None
        
        row = find_cell('B', self.shift_details["shift_date"])

        column_count = 0
        if self.request_sheet_break is True:
            for key in self.shift_details.keys():
                if key != "shift_date":
                    self.timesheet.range(f'{self.columns[column_count]}{row}').value = self.shift_details[key]
                    column_count += 1

        else:
            self.timesheet.range(f'{'D'}{row}').value = self.breaktime[0]
            self.timesheet.range(f'{'E'}{row}').value = self.breaktime[1]

            for key in self.shift_details.keys():
                if key != "shift_date":
                    if self.columns[column_count] != 'D' or 'E':
                        self.timesheet.range(f'{self.columns[column_count]}{row}').value = self.shift_details[key]
                        column_count += 1


    def calculate_break_time(self, start, end, break_duration_minutes):
        # Convert start and end times to total minutes
        start_hours, start_minutes = map(int, str(start).split('.'))
        end_hours, end_minutes = map(int, str(end).split('.'))
        start_total_minutes = start_hours * 60 + start_minutes
        end_total_minutes = end_hours * 60 + end_minutes

        # Calculate shift duration in minutes
        shift_duration_minutes = end_total_minutes - start_total_minutes

        # Calculate break start time in minutes to center the break
        break_start_minutes = start_total_minutes + (shift_duration_minutes - break_duration_minutes) / 2

        # Ensure break_start_minutes and break_end_minutes stay within a 24-hour format (1440 minutes)
        break_start_minutes = break_start_minutes % 1440
        break_end_minutes = (break_start_minutes + break_duration_minutes) % 1440

        # Convert break start and end times back to hours and minutes
        break_start_hours = int(break_start_minutes // 60)
        break_start_minutes = int(break_start_minutes % 60)
        break_end_hours = int(break_end_minutes // 60)
        break_end_minutes = int(break_end_minutes % 60)

        # Format break times
        break_start_formatted = f"{break_start_hours:02d}.{break_start_minutes:02d}"
        break_end_formatted = f"{break_end_hours:02d}.{break_end_minutes:02d}"

        self.breaktime = [float(break_start_formatted), float(break_end_formatted)]
    
    def save_sheet(self):
        self.file.save(f'demo\\src\\main\\resources\\timesheets\\{self.profile["last_name"]}_{self.profile["first_name"]}_Timesheet_{self.set_month}_{self.set_year}.xls')

    def finish(self):

        self.file.save(f'demo\\src\\main\\resources\\completed_timesheets\\{self.profile["last_name"]}_{self.profile["first_name"]}_Timesheet_{self.set_month}_{self.set_year}.xls')
        self.file.close()


main = timesheet_main()
has_profile = False
main_count = 0
sub_count = 0
while main_count != 1:

    user_input = input("")

    while user_input != "exit":

        if "SETUP_PROFILE" in user_input:
            details = user_input.split("=")
            main.setup_profile({"last_name": details[1], "first_name": details[2], "employee_number": details[3]})
            has_profile = True

        elif "SAVE_SHEET" in user_input:
            if has_profile is True:
                main.save_sheet()
            else:
                print("NO_PROFILE")
                break

        elif "MONTH" in user_input:
            details = user_input.split("=")
            main.set_month(details[-1])

        elif "YEAR" in user_input:
            details = user_input.split("=")
            main.set_year(details[-1])
        
        elif "MANUAL_ADD" in user_input:
            details = user_input.split("=")
            main.shift_details = {"shift_date": details[1], "start_time": details[2], "end_time": details[3], "break_time": details[4], "budget_code": details[5], "shift_type": details[6], "comments": details[7]}

            calculated_break = main.calculate_break_time(float(details[2]), float(details[3]), int(details[4]))
            main.append_to_sheet()

        elif "SET_DATE" in user_input:
            date = user_input.split("=")
            main.set_date(date[1], date[2])

        elif "FINISH" in user_input:
            main.finish()
            main_count = 1
            break

        user_input = input("")

