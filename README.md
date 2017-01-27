# CS583-Project-1

### Authors:
Murali Krishna Valluri (mvallu2@uic.edu)<br>
Spoorthi Pendyala (npendy2@uic.edu)

### Description:
Implementation of Apriori Algorithm with multiple MIS values and special
conditions.


### Input Format
    {20, 30, 80, 70, 50, 90}
    {20, 10, 80, 70}
    {10, 20, 80}
    {20, 30, 80}
    {20, 80}
    {20, 30, 80, 70, 50, 90, 100, 120, 140}

### Output Format
    Frequent 1-itemsets

	    5 : {20}
	    6 : {30}

	    Total number of freuqent 1-itemsets = 2


    Frequent 2-itemsets

	    3 : {30, 80}
    Tailcount = 6
	    4 : {40, 50}
    Tailcount = 5
	    5 : {30, 80}
    Tailcount = 7

	    Total number of freuqent 2-itemsets = 3

### Parameter List
    MIS(10) = 0.43
    MIS(20) = 0.30
    MIS(30) = 0.30
    MIS(40) = 0.40
    MIS(50) = 0.40
    MIS(60) = 0.30
    MIS(70) = 0.20
    MIS(80) = 0.20
    MIS(90) = 0.20
    MIS(100) = 0.10
    MIS(120) = 0.20
    MIS(140) = 0.15
    SDC = 0.1
    cannot_be_together: {20, 40}, {70, 80}
    must-have: 20 or 40 or 50

### Algorithm

    MS, SDC = Read parameter-file.txt
    T = Read input-data.txt
    F = DS();
    Call MS-Apriori(T, MS, SDC)
        - M = Sort(I, MS)
        - L = Init-Pass(T, M)
        - F1 = CheckSupValue(L)
        - F.Add(F1)
        - k = 2
        - n = T.Count
        While Fk.Count > 1
            If k = 2
                Ck = Level2-Candidate-Gen(L, SDC)
            Else
                Ck = MS-Candidate-Gen(Fk-1, SDC)
            End If

            For Each t in T
                For each c in Ck
                    If Subset(t, c)
                        c.Count++
                    End If

                    If Subset(t, c - c[1])
                        c.TailCount++
                    End If
                End For
            End For
            Fk = CheckSupValue(Ck)
            F.Add(Fk)
        End While
    End MS-Apriori
    Print F
