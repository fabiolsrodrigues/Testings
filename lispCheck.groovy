#!/usr/bin/env groovy

// Global Variables

String inputStrg
boolean retCd = true

String openChar = "\\("
String closChar = "\\)"

def openBlkMap = new LinkedHashMap()

int openInd = 0
int closInd = 0
int lineNum = 0
int linePos = 0

// Classes - Functions area

// Main Logic
if  ( ! binding.variables["args"] )
{
    return
}

if  ( this.args[0].equalsIgnoreCase("-f") )
{
    inputStrg = new File( this.args[1] ).text
}
else
{
    inputStrg = this.args[0]
}

inputStrg = inputStrg.normalize()

String[] openArr = inputStrg.split("\\(")
String[] closArr = inputStrg.split("\\)")

//println("This is the string: " + inputStrg)
numOpen = ( openArr.length - 1 )
numClos = ( closArr.length - 1 )

println("Number of Open  Parenthesis => " + numOpen )
println("Number of Close Parenthesis => " + numClos )

for( line in inputStrg.readLines() )
{
    lineNum += 1
    linePos = 0
    for ( strgChar in line.getChars() )
    {
        switch ( strgChar )
        {
            case '(' :
                openInd += 1
                openBlkMap.put( openInd.toString() , [lineNum.toString(), linePos.toString()] )
                break
            case ')' :
                if ( openInd == 0 )
                {
                    println("There is a closing block at line: " + lineNum + " position: " + linePos + " that was not opened .....")
                    retCd = false
                }
                else
                {
                    openBlkMap.remove( openInd.toString() )
                    openInd -= 1
                }
                break
        }
        linePos += 1
    }
}

if ( retCd )
{
    if ( openInd > 0 )
    {
        for ( openBlk in openBlkMap.values() )
        {
            println("The block at line: " + openBlk[0] + " position: " + openBlk[1] + " was not closed .....")
        }
        retCd = false
    }
    else
    {
        print("All blocks are perfectly defined")
        retCd = true
    }
}

return(retCd)