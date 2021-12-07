run-all:
    find . -maxdepth 1 -type f -name 'day*.sc' | sort | xargs -n1 amm

run target:
    amm -w {{target}}
