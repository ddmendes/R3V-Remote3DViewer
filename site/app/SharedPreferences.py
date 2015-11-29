import os.path


class AnglesStorage():

    def __init__(self, fname='angles.dat', defaultPitch=0.0, defaultYaw=0.0):
        self.fname = fname
        if os.path.exists(fname):
            f = open(fname, 'r+')

            self.pitch = int(f.readline().rstrip('\n'))
            if self.pitch is "":
                self.pitch = defaultPitch

            self.yaw = int(f.readline().rstrip('\n'))
            if self.yaw is "":
                self.yaw = defaultYaw

            f.close()

        else:
            self.pitch = defaultPitch
            self.yaw = defaultYaw

    def done(self):
        f = open(self.fname, 'w+')
        f.write(str(self.pitch) + '\n' + str(self.yaw) + '\n')
        f.flush()
        f.close()
