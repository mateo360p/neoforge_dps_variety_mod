dummyList = {
    ("WOOD", 59),
    ("STONE", 131),
    ("IRON", 250),
    ("GOLD", 32),
    ("DIAMOND", 1561),
    ("NETHERITE", 2031)
}

pickaxeCost = 3
hammerCost = 19
maxBlocksPerUse = 9

for cur in dummyList:
    dur = cur[1]

    costAverage = int(hammerCost/pickaxeCost) * dur
    maxUses = dur * maxBlocksPerUse

    #print(costAverage)
    #print(maxUses)

    print(cur[0] + ": " + str(int((costAverage - maxUses * 0.05))))

#input()