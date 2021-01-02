import os


def exactly(string, times):
    counts = {}
    for c in string:
        counts[c] = counts.get(c, 0) + 1
    for v in counts.values():
        if v == times:
            return True
    return False


input_file = "/input.txt"
with open(f".{input_file}" if len(os.path.dirname(__file__)) == 0 else os.path.dirname(__file__) + input_file,
          'r') as file:
    count_three = 0
    count_two = 0
    for line in file:
        fullmatch_three = exactly(line.strip(), 3)
        fullmatch_two = exactly(line.strip(), 2)
        if fullmatch_three:
            count_three += 1
        if fullmatch_two:
            count_two += 1

    print("step1", count_two, count_three, count_two * count_three)


