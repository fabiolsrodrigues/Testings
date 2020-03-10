#!/usr/bin/env groovy

// Imports

// Global Variables

String inputStrg
boolean retCd = true

// Methods area

// Main Logic
//if  ( ! binding.variables["args"] )
//{
//    return
//}

fieldSep = ','
fileName = "demoData.csv"
argInd = 0
for ( arg in args )
{
    switch ( args[argInd] )
    {
        case '-s' :
            argInd += 1
            fieldSep = args[argInd]
            break
        case '-f' :
            argInd += 1
            fileName = args[argInd]
            break
    }
    argInd += 1
}

inputFile = new File( fileName ).text
inputFile = inputFile.normalize()

insuranceList = []
userMap = [:]

for( fileLine in inputFile.readLines() )
{
    def tokens = fileLine.tokenize(fieldSep)
    String lineUserId       = tokens[0]
    String lineUserFirst    = tokens[1]
    String lineUserLast     = tokens[2]
    int lineUserVersion     = tokens[3] as Integer
    String lineInsurance    = tokens[4]

    String userKey    = lineInsurance  + '|' + lineUserLast + '|' + lineUserFirst + '|' + lineUserId

    // Saves User record
    if ( userMap.containsKey(userKey)  )
    {
        if ( userMap.get(userKey) < lineUserVersion )
        {
            userMap.put(userKey, lineUserVersion)
        }
    }
    else
    {
        userMap.put(userKey, lineUserVersion)
    }

    // Saves Insurance name in a List
    if ( lineInsurance !in insuranceList )
    {
        insuranceList.add(lineInsurance)
    }
}

unsortedUserMap = userMap
sortedUserMap = unsortedUserMap.sort()
prevInsurance = ""
File outFile

for ( userEntry in sortedUserMap )
{
    userKey     = userEntry.key
    userValue   = userEntry.value
    def newTokens = userKey.tokenize('|')
    String userId       = newTokens[3]
    String userFirst    = newTokens[2]
    String userLast     = newTokens[1]
    int userVersion     = userValue
    String insurance    = newTokens[0]

    outLine = userId + ',' + userFirst + ',' + userLast + ',' + userVersion.toString() + ',' + insurance + '\n'
    if ( insurance.equalsIgnoreCase(prevInsurance) )
    {
        outFile.append(outLine)
    }
    else
    {
//        if ( prevInsurance != "" )
//        {
//            outFile.close()
//        }
        outFileName = insurance + '.csv'
        outFile = new File("$outFileName")
        outFile.append(outLine)
        prevInsurance = insurance
    }
}
//if ( prevInsurance != "" )
//{
//    outFile.close()
//}

return(0)