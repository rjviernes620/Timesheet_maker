from xlwings import Book
from datetime import datetime
from docx import Document
from request_sheet import *

class timesheet_main:

    def __init__(self):
        self.file = Book('demo\\src\\main\\resources\\main.xls')
        self.timesheet = self.file.sheets['Sheet1']
        self.shift_details = "placeholder"
        self.columns = ['C', 'D', 'E', 'F', 'H', 'J', 'N']
        self.profile = dict()
        self.set_month = "placeholder"
        self.set_year = "placeholder"

    def setup_profile(self, details):
        self.timesheet.range('K2').value = details["last_name"]
        self.timesheet.range('K4').value = details["first_name"]
        self.timesheet.range('F4').value = details["employee_number"]
        self.profile = details

    def set_date(self, year, month):
        self.timesheet.range('K7').value = year
        self.set_year = year
        self.timesheet.range('L7').value = month
        self.set_month = month

    def current_date(self):
        self.timesheet.range('G58').value = datetime.now().day
        self.timesheet.range('H58').value = datetime.now().strftime('%B')
        self.timesheet.range('I58').value = datetime.now().year

    def request_sheet(self, file_path):
        doc = Document(file_path)
        request = request_sheet(doc)
        self.shift_details = request.collate_to_dict()
        return self.shift_details

    def append_to_sheet(self):
        
        def find_cell(column, target):
            for i in range(13, 43):
                if self.timesheet.range(f'{column}{i}').value == target:
                    return i
            return None
        
        row = find_cell('B', self.shift_details["shift_date"])

        column_count = 0
        for key in self.shift_details.keys():
            if key != "shift_date":
                self.timesheet.range(f'{self.columns[column_count]}{row}').value = self.shift_details[key]
                column_count += 1

    def finish(self):

        self.file.save(f'demo\\src\\main\\resources\\completed_timesheets\\{self.profile["last_name"]}_{self.profile["first_name"]}_Timesheet_{self.set_month}_{self.set_year}.xls')
        self.file.close()


main = timesheet_main()
user_input = input("")
while True:

    

    while user_input != "exit":

        if "SETUP_PROFILE" in user_input:
            details = user_input.split(" ")
            main.setup_profile({"last_name": details[1], "first_name": details[2], "employee_number": details[3]})
