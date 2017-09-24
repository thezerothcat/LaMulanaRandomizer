#!/usr/bin/python3
import os.path
from struct import unpack

if os.path.exists(r"E:\Steam\steamapps\common\La-Mulana"):
    lmpath = r"E:\Steam\steamapps\common\La-Mulana"
elif os.path.exists(r"C:\GOG Games\La-Mulana"):
    lmpath = r"C:\GOG Games\La-Mulana"
    # let's support the default folder maybe
else:
    print("Cannot find La-Mulana")
    assert False

mappath = os.path.join(lmpath, "data", "mapdata")



class Zone:
    def __init__(self):
        self.rooms = []
    def __str__(self):
        ret = 'ZONE %02d "%s"' % (self.idx, self.name)
        if self.objs:
            ret += '\n' + '\n'.join(map(str, self.objs))
        ret += '\n' + '\n'.join(map(str, self.rooms))
        return ret
class Room:
    def __init__(self):
        self.screens = []
    def __str__(self):
        ret = 'ROOM %02d-%02d' % self.idx
        if self.objs:
            ret += '\n' + '\n'.join(map(str, self.objs))
        ret += '\n' + '\n'.join(map(str, self.screens))
        return ret
class Screen:
    def __str__(self):
        ret = 'SCREEN %02d-%02d-%02d "%s"' % (*self.idx, self.name)
        if self.objs:
            ret += '\n' + '\n'.join(map(str, self.objs))
        for i, e in enumerate(self.exits):
            if e[0] >= 0:
                ret += '\n%s %02d-%02d-%02d' % (('Up', 'Right', 'Down', 'Left')[i], *e)
        return ret
class ObjectCreate:
    def __str__(self):
        ret = 'OBJECT Type=0x%x' % self.type
        if self.pos[0] >= 0:
            ret += ' @ %d, %d' % self.pos
        if self.test:
            ret += '\nTEST:\n%s' % '\n'.join(map(str, self.test))
        if self.write:
            ret += '\nUPDATE:\n%s' % '\n'.join(map(str, self.write))
        ret += '\n' + '\n'.join(['ARG %d: %d' % (i, v) for i,v in enumerate(self.args)])
        return ret
class ByteOp:
    op_fmts = {
         0: 'Clear all bytes',
         1: '[{0}]  = {1}',
         2: '[{0}]',
         3: '[{0}]  = 0',
         4: '[{0}] &= 0x{1:x}',
         5: '[{0}] |= 0x{1:x}',
         6: '[{0}] ^= 0x{1:x}',
         7: '[{0}] += {1}',
         8: '[{0}] -= {1}',
         9: '[{0}] *= {1}',
        10: '[{0}] /= {1}',
        11: '[{0}] == {1}',
        12: '[{0}] <= {1}',
        13: '[{0}] >= {1}',
        14: '[{0}] != {1}',
        15: '[{0}]  > {1}',
        16: '[{0}]  < {1}',
        17: '[{0}]  & 0x{1:x} != 0',
        18: '[{0}]  | 0x{1:x} != 0',
        19: '[{0}]  ^ 0x{1:x} != 0',
        20: '[{0}] == 0 // buggy, actually always true',
        21: '[{0}]  & 0x{1:x} == 0',
        22: '[{0}]  | 0x{1:x} == 0',
        23: '[{0}]  ^ 0x{1:x} == 0 // buggy, actually always true',
        24: '[{0}] != 0',
        25: 'NOP {0} {1}',
    }
    def __str__(self):
        return self.op_fmts[self.op].format('%04x' % self.idx, self.value)

testop = {
    0x00: 11,
    0x01: 12,
    0x02: 13,
    0x03: 17,
    0x04: 18,
    0x05: 19,
    0x06: 20,
    0x40: 14,
    0x41: 15,
    0x42: 16,
    0x43: 21,
    0x44: 22,
    0x45: 23,
    0x46: 24,
}
writeop = {
    0:  1,
    1:  7,
    2:  8,
    3:  9,
    4: 10,
    5:  4,
    6:  5,
    7:  6,
}

def readbyteop(f, writes = False):
    ret = ByteOp()
    ret.idx, ret.value, op = unpack('>HBB', f.read(4))
    ret.op = (writeop if writes else testop).get(op, 25)
    return ret

def readobj(f, haspos = False):
    ret = ObjectCreate()
    ret.type, c, argc = unpack('>HBB', f.read(4))
    write_count = c & 0xf
    test_count = c >> 4
    if haspos:
        ret.pos = tuple([20 * x for x in unpack('>HH', f.read(4))])
    else:
        ret.pos = -1, -1
    ret.test = [readbyteop(f, False) for i in range(test_count)]
    ret.write = [readbyteop(f, True) for i in range(write_count)]
    ret.args = list(unpack('>%dh' % argc, f.read(2 * argc)))
    return ret


rcd = open(os.path.join(mappath, "script.rcd"), "rb")
rcd.read(2) # unk, '\0\0'


zones = []
printFrame = True
for z in range(26):
    msd = open(os.path.join(mappath, "map%.2d.msd" % z), 'rb')
    zone = Zone()
    zones.append(zone)
    zone.idx = z
    namelen, objcount = unpack('>BH', rcd.read(3))
    #print('%s %s' % (namelen, objcount))
    zone.name = rcd.read(namelen).decode('utf_16_be')
    zone.objs = [readobj(rcd) for i in range(objcount)]
    while True:
        frames = unpack('>H', msd.read(2))[0]
        if(printFrame):
            print(frames)
        printFrame = False
        if frames == 0:
            break
        msd.seek(frames * 2, 1)
    rooms = unpack('>BBB', msd.read(3))[2]
    for r in range(rooms):
        room = Room()
        zone.rooms.append(room)
        room.idx = z, r
        objcount = unpack('>H', rcd.read(2))[0]
        room.objs = [readobj(rcd) for i in range(objcount)]
        
        room.layers, room.pr_layer = unpack('>BBB', msd.read(3))[1:]
        room.hit_w, room.hit_h = unpack('>2H', msd.read(4))
        msd.seek(room.hit_w * room.hit_h, 1)
        
        for l in range(room.layers):
            w, h, sublayers = unpack('>2HB', msd.read(5))
            if l == room.pr_layer:
                room.tile_w, room.tile_h = w, h
                room.screen_w = room.tile_w // 32
                room.screen_h = room.tile_h // 24
                room.numscreens = room.screen_w * room.screen_h
            msd.seek(sublayers * w * h * 2, 1)
        for s in range(room.numscreens):
            screen = Screen()
            room.screens.append(screen)
            screen.idx = z, r, s
            namelen, count_objs, count_nopos = unpack('>BHB', rcd.read(4))
            screen.objs = [readobj(rcd, False) for i in range(count_nopos)]
            screen.objs += [readobj(rcd, True) for i in range(count_objs - count_nopos)]
            screen.name = rcd.read(namelen).decode('utf_16_be')
            screen.exits = [unpack('>bbb', rcd.read(3)) for i in range(4)]

rcd.close() #tsk


if __name__ == "__main__":
	dec = open("rcdout.txt", "w", encoding="utf_8")

	for zone in zones:
		dec.write(str(zone))
