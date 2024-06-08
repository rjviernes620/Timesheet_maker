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

    def setup_profile(self, details):
        self.timesheet.range('K2').value = details["last_name"]
        self.timesheet.range('K4').value = details["first_name"]
        self.timesheet.range('F4').value = details["employee_number"]

    def set_date(self, year, month):
        self.timesheet.range('K7').value = year
        self.timesheet.range('L7').value = month

    def current_date(self):
        self.timesheet.range('G58').value = datetime.now().day
        self.timesheet.range('H58').value = datetime.now().strftime('%B')
        self.timesheet.range('I58').value = datetime.now().year

    def request_sheet(self):
        doc = Document('demo\\src\\main\\resources\\test1.docx')
        request = request_sheet.py(doc)
        self.shift_details = request.collate_to_dict()
        return self.shift_details

    def append_to_sheet(self):
        
        def find_cell(column, target):
            for i in range(13, 43):
                if self.timesheet.range(f'{column}{i}').value == target:
                    return i
            return None
        
        row = find_cell('B', self.shift_details["shift_date"])

        for column in self.columns:
            for key, values in self.shift_details:
                if key != "shift_date":
                    self.timesheet.range(f'{column}{row}').value = values[key]


if __name__ == "__main__":
    main = timesheet_main()
    main.setup_profile({"last_name": "Doe", "first_name": "John", "employee_number": "123456"})
    main.set_date(2024, 'May')
    main.current_date()
    main.request_sheet()
    main.append_to_sheet()

