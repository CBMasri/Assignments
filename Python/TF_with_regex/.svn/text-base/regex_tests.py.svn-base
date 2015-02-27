import re
# -------------------------------------------- #
def main():
    test()
# -------------------------------------------- #
def test():
    print ("Testing Command Sequences:")
    
    # should match control sequences.
    test_cases = [
        # these should match
        "?fmt on",
        "?fmt off",
        "?fmt +on",
        "?fmt -off",
        "?mrgn 30",
        "?mrgn -40",
        "?mrgn +50",
        "?width 60",
        "?width 70",
        "?mrgn +1000",
        # these shouldn't match
        "?Fmt on",
        "?Fmt off",
        "?fmt potato",
        "?fmt 30+",
        "?mrgn ++1",
        "?fmt ++2",
        "Not command",
        "?still nope",
        "?margin +20",
        "?Is too long",
        "lalalalala",
        "?margin +20",
    ]

    regex = re.compile(r"(\?mrgn|\?fmt|\?width) (\-|\+)?(\d+|on|off)\s*$")
    for test in test_cases:
        match = regex.search(test)
        if match:
            print ("Good match: " + str(match.groups()) )
        else:
            print ("Not a match: " + test)
# -------------------------------------------- #
if __name__ == "__main__":
    main()