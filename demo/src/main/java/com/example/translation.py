class insert_to_Timesheet:
    
    def __init__(self, placeholder):
        self.placeholder = placeholder
        self.timesheet = Timesheet()
        self.timesheet.insert(self.placeholder)
        self.timesheet.save()


    def prepare_timesheet(self):
        self.timesheet.prepare()
        self.timesheet.save()