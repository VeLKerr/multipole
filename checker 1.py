# python 3.3
# Solution checker
# For details see: http://goo.gl/ruHIYp

from collections import defaultdict
elements = dict()
arcs = defaultdict( list )

inputFilename = r'input.txt'

with open(inputFilename, 'r') as infile:
    for line in infile:
        line = line.strip()
        if len(line) == 0:
            continue
        tokens = line.split()
        if len(tokens) != 2:
            print('Wrong file format')
            exit()
        if tokens[1] in ('AND', 'OR', 'NOT'):
            elements[int(tokens[0])] = tokens[1]
            continue
        tokens = list(map( int, tokens ))
        arcs[tokens[1]].append( tokens[0] )

if set( elements.keys() ) != set( range( 3, 256 ) ):
    print('Wrong file format')
    exit()
    
if set( arcs.keys() ) != set( range( 3, 256 ) ):
    print('Wrong file format')
    exit()
    
for i in range( 3, 256 ):
    if ( elements[i], len( arcs[i] ) ) not in { ( 'AND', 2 ), ( 'OR', 2 ), ( 'NOT', 1 ) }:
        print('Wrong file format')
        exit()


evaluations = dict()
evaluations[0] = 0xF0
evaluations[1] = 0xCC
evaluations[2] = 0xAA

def evaluateAtNode( node ):
    global evaluations
    if node in evaluations:
        pass
    elif elements[node] == 'AND':
        evaluations[node] = evaluateAtNode( arcs[node][0] ) & evaluateAtNode( arcs[node][1] )
    elif elements[node] == 'OR':
        evaluations[node] = evaluateAtNode( arcs[node][0] ) | evaluateAtNode( arcs[node][1] )
    elif elements[node] == 'NOT':
        evaluations[node] = evaluateAtNode( arcs[node][0] ) ^ 0xFF
    return evaluations[node]

for node in range( 256 ):
    evaluateAtNode( node )

if set( evaluations.values() ) == set( range(256) ):
    print('Correct solution!')
else:
    print('Wrong solution!')